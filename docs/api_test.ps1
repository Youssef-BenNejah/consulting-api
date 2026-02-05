$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Net.Http
$base = "http://localhost:8081/api/v1"
$now = Get-Date

function Invoke-Json($method, $url, $body, $token) {
  $headers = @{}
  if ($token) { $headers['Authorization'] = "Bearer $token" }
  try {
    $resp = Invoke-WebRequest -Method $method -Uri $url -ContentType 'application/json' -Body ($body | ConvertTo-Json -Depth 10) -Headers $headers -ErrorAction Stop
    return @{ ok=$true; status=$resp.StatusCode; body=$resp.Content }
  } catch {
    $resp = $_.Exception.Response
    if ($resp -ne $null) {
      $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
      $bodyText = $reader.ReadToEnd()
      return @{ ok=$false; status=$resp.StatusCode.value__; body=$bodyText }
    }
    return @{ ok=$false; status=$null; body=$_.Exception.Message }
  }
}

function Invoke-Get($url, $token) {
  $headers = @{}
  if ($token) { $headers['Authorization'] = "Bearer $token" }
  try {
    $resp = Invoke-WebRequest -Method Get -Uri $url -Headers $headers -ErrorAction Stop
    return @{ ok=$true; status=$resp.StatusCode; body=$resp.Content }
  } catch {
    $resp = $_.Exception.Response
    if ($resp -ne $null) {
      $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
      $bodyText = $reader.ReadToEnd()
      return @{ ok=$false; status=$resp.StatusCode.value__; body=$bodyText }
    }
    return @{ ok=$false; status=$null; body=$_.Exception.Message }
  }
}

function Invoke-Delete($url, $token) {
  $headers = @{}
  if ($token) { $headers['Authorization'] = "Bearer $token" }
  try {
    $resp = Invoke-WebRequest -Method Delete -Uri $url -Headers $headers -ErrorAction Stop
    return @{ ok=$true; status=$resp.StatusCode; body=$resp.Content }
  } catch {
    $resp = $_.Exception.Response
    if ($resp -ne $null) {
      $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
      $bodyText = $reader.ReadToEnd()
      return @{ ok=$false; status=$resp.StatusCode.value__; body=$bodyText }
    }
    return @{ ok=$false; status=$null; body=$_.Exception.Message }
  }
}

function Invoke-MultipartUpload($url, $filePath, $fields, $token) {
  $http = New-Object System.Net.Http.HttpClient
  if ($token) { $http.DefaultRequestHeaders.Authorization = [System.Net.Http.Headers.AuthenticationHeaderValue]::new("Bearer", $token) }
  $mp = New-Object System.Net.Http.MultipartFormDataContent
  $fs = [System.IO.File]::OpenRead($filePath)
  $fileContent = New-Object System.Net.Http.StreamContent($fs)
  $fileContent.Headers.ContentType = [System.Net.Http.Headers.MediaTypeHeaderValue]::Parse("image/webp")
  $mp.Add($fileContent, "file", [System.IO.Path]::GetFileName($filePath))
  foreach ($k in $fields.Keys) {
    $mp.Add(([System.Net.Http.StringContent]::new([string]$fields[$k])), $k)
  }
  $resp = $http.PostAsync($url, $mp).Result
  $body = $resp.Content.ReadAsStringAsync().Result
  $fs.Close()
  $http.Dispose()
  return @{ ok=($resp.StatusCode.value__ -ge 200 -and $resp.StatusCode.value__ -lt 300); status=$resp.StatusCode.value__; body=$body }
}

$report = New-Object System.Text.StringBuilder
$null = $report.AppendLine("# API Endpoint Test Report")
$null = $report.AppendLine("")
$null = $report.AppendLine("- Generated: $($now.ToString('yyyy-MM-dd HH:mm:ss'))")
$null = $report.AppendLine("- Base URL: $base")
$null = $report.AppendLine("")

# Register + login (loop until success)
$password = "P@ssword1!"
$token = $null
$email = $null
$phone = $null
for ($i=1; $i -le 10; $i++) {
  $ts = Get-Date -Format "yyyyMMddHHmmssfff"
  $email = "test$ts@example.com"
  $phone = "+21699$((Get-Random -Minimum 100000 -Maximum 999999))"
  $registerBody = @{ firstName="Test"; lastName="User"; email=$email; phoneNumber=$phone; password=$password; confirmPassword=$password }
  $reg = Invoke-Json 'Post' "$base/auth/register" $registerBody $null
  if ($reg.status -eq 201 -or $reg.status -eq 200) {
    $loginBody = @{ email=$email; password=$password }
    $login = Invoke-Json 'Post' "$base/auth/login" $loginBody $null
    if ($login.status -eq 200) {
      $token = ( $login.body | ConvertFrom-Json ).access_token
      break
    }
  }
}

$null = $report.AppendLine("## Auth")
$null = $report.AppendLine("- Registered email: $email")
$null = $report.AppendLine("- Registered phone: $phone")
$null = $report.AppendLine("- Login token acquired: $([bool]$token)")
$null = $report.AppendLine("")

# Seed base data
$clientReq = @{ name="Client $($now:yyyyMMddHHmmss)"; industry="IT"; country="US"; primaryContact="Test User"; email=$email; phone="12025550123"; contractType="Enterprise"; notes="Created by API"; tags=@("tag1","tag2") }
$clientCreate = Invoke-Json 'Post' "$base/clients" $clientReq $token
$clientId = $null; $clientName = $null
if ($clientCreate.status -in 200,201) { $clientId = ( $clientCreate.body | ConvertFrom-Json ).data.id; $clientName = ( $clientCreate.body | ConvertFrom-Json ).data.name }

$projectReq = @{ projectId="PRJ-$($now:yyyyMMddHHmmss)"; clientId=$clientId; clientName=$clientName; name="Project $($now:yyyyMMddHHmmss)"; description="Test project"; startDate="2026-02-05"; endDate="2026-03-05"; status="delivery"; type="fixed"; clientBudget=120000; vendorCost=40000; internalCost=30000; healthStatus="green"; progress=20; openTasks=5; openIssues=1; openRisks=1 }
$projectCreate = Invoke-Json 'Post' "$base/projects" $projectReq $token
$projectId = $null; $projectName = $null
if ($projectCreate.status -in 200,201) { $projectId = ( $projectCreate.body | ConvertFrom-Json ).data.id; $projectName = ( $projectCreate.body | ConvertFrom-Json ).data.name }

function Add-Case($sb, $title, $resp, $request) {
  $null = $sb.AppendLine("### $title")
  if ($request -ne $null) {
    $null = $sb.AppendLine("Request:")
    $null = $sb.AppendLine('```json')
    $null = $sb.AppendLine(($request | ConvertTo-Json -Depth 10))
    $null = $sb.AppendLine('```')
  }
  $null = $sb.AppendLine("Response: status=$($resp.status)")
  $null = $sb.AppendLine('```json')
  $null = $sb.AppendLine(($resp.body))
  $null = $sb.AppendLine('```')
  $null = $sb.AppendLine("")
}
# Clients
$null = $report.AppendLine("## /clients")
Add-Case $report "Create client (happy)" $clientCreate $clientReq
Add-Case $report "Create client (missing required)" (Invoke-Json 'Post' "$base/clients" @{} $token) @{}
Add-Case $report "List clients (happy)" (Invoke-Get "$base/clients" $token) $null
Add-Case $report "List clients (invalid page)" (Invoke-Get "$base/clients?page=-1&size=0" $token) $null
Add-Case $report "Get client (happy)" (Invoke-Get "$base/clients/$clientId" $token) $null
Add-Case $report "Get client (not found)" (Invoke-Get "$base/clients/does-not-exist" $token) $null
Add-Case $report "Update client (happy)" (Invoke-Json 'Patch' "$base/clients/$clientId" @{ industry="FinTech"; notes="Updated" } $token) @{ industry="FinTech"; notes="Updated" }
Add-Case $report "Update client (invalid)" (Invoke-Json 'Patch' "$base/clients/$clientId" @{ } $token) @{}
Add-Case $report "Delete client (invalid id)" (Invoke-Delete "$base/clients/does-not-exist" $token) $null

# Projects
$null = $report.AppendLine("## /projects")
Add-Case $report "Create project (happy)" $projectCreate $projectReq
Add-Case $report "Create project (missing required)" (Invoke-Json 'Post' "$base/projects" @{} $token) @{}
Add-Case $report "Create project (invalid enum)" (Invoke-Json 'Post' "$base/projects" (@{ projectId="PRJ-X"; clientId=$clientId; clientName=$clientName; name="Bad"; description="Bad"; startDate="2026-02-05"; endDate="2026-03-05"; status="bad"; type="fixed"; clientBudget=1; vendorCost=1; internalCost=1; healthStatus="green"; progress=0; openTasks=0; openIssues=0; openRisks=0 }) $token) @{ status="bad" }
Add-Case $report "List projects (happy)" (Invoke-Get "$base/projects" $token) $null
Add-Case $report "Get project (happy)" (Invoke-Get "$base/projects/$projectId" $token) $null
Add-Case $report "Get project (not found)" (Invoke-Get "$base/projects/does-not-exist" $token) $null
Add-Case $report "Update project (happy)" (Invoke-Json 'Patch' "$base/projects/$projectId" @{ progress=40; healthStatus="amber"; status="review" } $token) @{ progress=40; healthStatus="amber"; status="review" }
Add-Case $report "Update project (invalid)" (Invoke-Json 'Patch' "$base/projects/$projectId" @{ progress=200 } $token) @{ progress=200 }
Add-Case $report "Delete project (invalid id)" (Invoke-Delete "$base/projects/does-not-exist" $token) $null

# Document categories
$null = $report.AppendLine("## /document-categories")
$docCatReq = @{ name="Contract"; key="contract-$($now:HHmmss)"; color="#0055FF" }
$docCatCreate = Invoke-Json 'Post' "$base/document-categories" $docCatReq $token
$docCatId = $null; if ($docCatCreate.status -in 200,201) { $docCatId = ( $docCatCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create document category (happy)" $docCatCreate $docCatReq
Add-Case $report "Create document category (missing required)" (Invoke-Json 'Post' "$base/document-categories" @{} $token) @{}
Add-Case $report "List document categories" (Invoke-Get "$base/document-categories" $token) $null
Add-Case $report "Get document category (happy)" (Invoke-Get "$base/document-categories/$docCatId" $token) $null
Add-Case $report "Get document category (not found)" (Invoke-Get "$base/document-categories/does-not-exist" $token) $null
Add-Case $report "Update document category (happy)" (Invoke-Json 'Patch' "$base/document-categories/$docCatId" @{ color="#000000" } $token) @{ color="#000000" }
Add-Case $report "Delete document category (invalid id)" (Invoke-Delete "$base/document-categories/does-not-exist" $token) $null

# Project types
$null = $report.AppendLine("## /project-types")
$projTypeReq = @{ name="Fixed Price"; key="fixed-$($now:HHmmss)"; description="Fixed scope" }
$projTypeCreate = Invoke-Json 'Post' "$base/project-types" $projTypeReq $token
$projTypeId = $null; if ($projTypeCreate.status -in 200,201) { $projTypeId = ( $projTypeCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create project type (happy)" $projTypeCreate $projTypeReq
Add-Case $report "Create project type (missing required)" (Invoke-Json 'Post' "$base/project-types" @{} $token) @{}
Add-Case $report "List project types" (Invoke-Get "$base/project-types" $token) $null
Add-Case $report "Get project type (happy)" (Invoke-Get "$base/project-types/$projTypeId" $token) $null
Add-Case $report "Get project type (not found)" (Invoke-Get "$base/project-types/does-not-exist" $token) $null
Add-Case $report "Update project type (happy)" (Invoke-Json 'Patch' "$base/project-types/$projTypeId" @{ description="Updated" } $token) @{ description="Updated" }
Add-Case $report "Delete project type (invalid id)" (Invoke-Delete "$base/project-types/does-not-exist" $token) $null

# Contract types
$null = $report.AppendLine("## /contract-types")
$contractTypeReq = @{ name="Enterprise"; key="enterprise-$($now:HHmmss)"; description="Enterprise agreement" }
$contractTypeCreate = Invoke-Json 'Post' "$base/contract-types" $contractTypeReq $token
$contractTypeId = $null; if ($contractTypeCreate.status -in 200,201) { $contractTypeId = ( $contractTypeCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create contract type (happy)" $contractTypeCreate $contractTypeReq
Add-Case $report "Create contract type (missing required)" (Invoke-Json 'Post' "$base/contract-types" @{} $token) @{}
Add-Case $report "List contract types" (Invoke-Get "$base/contract-types" $token) $null
Add-Case $report "Get contract type (happy)" (Invoke-Get "$base/contract-types/$contractTypeId" $token) $null
Add-Case $report "Get contract type (not found)" (Invoke-Get "$base/contract-types/does-not-exist" $token) $null
Add-Case $report "Update contract type (happy)" (Invoke-Json 'Patch' "$base/contract-types/$contractTypeId" @{ description="Updated" } $token) @{ description="Updated" }
Add-Case $report "Delete contract type (invalid id)" (Invoke-Delete "$base/contract-types/does-not-exist" $token) $null

# Opportunities
$null = $report.AppendLine("## /opportunities")
$oppReq = @{ clientId=$clientId; clientName=$clientName; title="New rollout"; expectedValue=90000; probability=45; stage="proposal"; expectedCloseDate="2026-03-01"; notes="Awaiting review" }
$oppCreate = Invoke-Json 'Post' "$base/opportunities" $oppReq $token
$oppId = $null; if ($oppCreate.status -in 200,201) { $oppId = ( $oppCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create opportunity (happy)" $oppCreate $oppReq
Add-Case $report "Create opportunity (missing required)" (Invoke-Json 'Post' "$base/opportunities" @{} $token) @{}
Add-Case $report "Create opportunity (invalid enum)" (Invoke-Json 'Post' "$base/opportunities" (@{ clientId=$clientId; clientName=$clientName; title="Bad"; expectedValue=1; probability=1; stage="bad"; expectedCloseDate="2026-03-01" }) $token) @{ stage="bad" }
Add-Case $report "List opportunities" (Invoke-Get "$base/opportunities" $token) $null
Add-Case $report "Get opportunity (happy)" (Invoke-Get "$base/opportunities/$oppId" $token) $null
Add-Case $report "Get opportunity (not found)" (Invoke-Get "$base/opportunities/does-not-exist" $token) $null
Add-Case $report "Update opportunity (happy)" (Invoke-Json 'Patch' "$base/opportunities/$oppId" @{ notes="Updated" } $token) @{ notes="Updated" }
Add-Case $report "Delete opportunity (invalid id)" (Invoke-Delete "$base/opportunities/does-not-exist" $token) $null

# Communication logs
$null = $report.AppendLine("## /communication-logs")
$commReq = @{ clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; date="2026-02-05"; type="meeting"; summary="Weekly sync"; actionItems=@("Send summary"); participants=@("Alice","Bob") }
$commCreate = Invoke-Json 'Post' "$base/communication-logs" $commReq $token
$commId = $null; if ($commCreate.status -in 200,201) { $commId = ( $commCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create communication log (happy)" $commCreate $commReq
Add-Case $report "Create communication log (missing required)" (Invoke-Json 'Post' "$base/communication-logs" @{} $token) @{}
Add-Case $report "Create communication log (invalid enum)" (Invoke-Json 'Post' "$base/communication-logs" (@{ clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; date="2026-02-05"; type="bad"; summary="Bad"; actionItems=@(); participants=@() }) $token) @{ type="bad" }
Add-Case $report "List communication logs" (Invoke-Get "$base/communication-logs" $token) $null
Add-Case $report "Get communication log (happy)" (Invoke-Get "$base/communication-logs/$commId" $token) $null
Add-Case $report "Get communication log (not found)" (Invoke-Get "$base/communication-logs/does-not-exist" $token) $null
Add-Case $report "Update communication log (happy)" (Invoke-Json 'Patch' "$base/communication-logs/$commId" @{ summary="Updated" } $token) @{ summary="Updated" }
Add-Case $report "Delete communication log (invalid id)" (Invoke-Delete "$base/communication-logs/does-not-exist" $token) $null

# Epics
$null = $report.AppendLine("## /epics")
$epicReq = @{ projectId=$projectId; projectName=$projectName; title="Platform Migration"; description="Migrate core services"; priority="must"; status="in-progress"; progress=15; storyCount=4 }
$epicCreate = Invoke-Json 'Post' "$base/epics" $epicReq $token
$epicId = $null; if ($epicCreate.status -in 200,201) { $epicId = ( $epicCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create epic (happy)" $epicCreate $epicReq
Add-Case $report "Create epic (missing required)" (Invoke-Json 'Post' "$base/epics" @{} $token) @{}
Add-Case $report "Create epic (invalid enum)" (Invoke-Json 'Post' "$base/epics" (@{ projectId=$projectId; projectName=$projectName; title="Bad"; description="Bad"; priority="bad"; status="bad"; progress=0; storyCount=0 }) $token) @{ priority="bad"; status="bad" }
Add-Case $report "List epics" (Invoke-Get "$base/epics" $token) $null
Add-Case $report "Get epic (happy)" (Invoke-Get "$base/epics/$epicId" $token) $null
Add-Case $report "Get epic (not found)" (Invoke-Get "$base/epics/does-not-exist" $token) $null
Add-Case $report "Update epic (happy)" (Invoke-Json 'Patch' "$base/epics/$epicId" @{ progress=30 } $token) @{ progress=30 }
Add-Case $report "Delete epic (invalid id)" (Invoke-Delete "$base/epics/does-not-exist" $token) $null

# User stories
$null = $report.AppendLine("## /user-stories")
$userStoryReq = @{ epicId=$epicId; epicTitle="Platform Migration"; projectId=$projectId; projectName=$projectName; title="Migrate auth module"; description="Port auth service"; acceptanceCriteria=@("All tests pass"); priority="must"; status="backlog"; effort=8; notes="Needs review" }
$userStoryCreate = Invoke-Json 'Post' "$base/user-stories" $userStoryReq $token
$userStoryId = $null; if ($userStoryCreate.status -in 200,201) { $userStoryId = ( $userStoryCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create user story (happy)" $userStoryCreate $userStoryReq
Add-Case $report "Create user story (missing required)" (Invoke-Json 'Post' "$base/user-stories" @{} $token) @{}
Add-Case $report "Create user story (invalid enum)" (Invoke-Json 'Post' "$base/user-stories" (@{ epicId=$epicId; epicTitle="Platform Migration"; projectId=$projectId; projectName=$projectName; title="Bad"; description="Bad"; acceptanceCriteria=@("A"); priority="bad"; status="bad"; effort=1 }) $token) @{ priority="bad"; status="bad" }
Add-Case $report "List user stories" (Invoke-Get "$base/user-stories" $token) $null
Add-Case $report "Get user story (happy)" (Invoke-Get "$base/user-stories/$userStoryId" $token) $null
Add-Case $report "Get user story (not found)" (Invoke-Get "$base/user-stories/does-not-exist" $token) $null
Add-Case $report "Update user story (happy)" (Invoke-Json 'Patch' "$base/user-stories/$userStoryId" @{ notes="Updated" } $token) @{ notes="Updated" }
Add-Case $report "Delete user story (invalid id)" (Invoke-Delete "$base/user-stories/does-not-exist" $token) $null

# Tasks
$null = $report.AppendLine("## /tasks")
$taskReq = @{ title="Create migration plan"; description="Draft migration plan"; projectId=$projectId; projectName=$projectName; owner="Alice"; ownerType="internal"; dueDate="2026-02-08"; status="todo"; priority="must"; estimatedHours=16; actualHours=0 }
$taskCreate = Invoke-Json 'Post' "$base/tasks" $taskReq $token
$taskId = $null; if ($taskCreate.status -in 200,201) { $taskId = ( $taskCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create task (happy)" $taskCreate $taskReq
Add-Case $report "Create task (missing required)" (Invoke-Json 'Post' "$base/tasks" @{} $token) @{}
Add-Case $report "Create task (invalid enum)" (Invoke-Json 'Post' "$base/tasks" (@{ title="Bad"; description="Bad"; projectId=$projectId; projectName=$projectName; owner="Alice"; ownerType="bad"; dueDate="2026-02-08"; status="bad"; priority="bad"; estimatedHours=1; actualHours=0 }) $token) @{ ownerType="bad"; status="bad"; priority="bad" }
Add-Case $report "List tasks" (Invoke-Get "$base/tasks" $token) $null
Add-Case $report "Get task (happy)" (Invoke-Get "$base/tasks/$taskId" $token) $null
Add-Case $report "Get task (not found)" (Invoke-Get "$base/tasks/does-not-exist" $token) $null
Add-Case $report "Update task (happy)" (Invoke-Json 'Patch' "$base/tasks/$taskId" @{ status="in-progress" } $token) @{ status="in-progress" }
Add-Case $report "Delete task (invalid id)" (Invoke-Delete "$base/tasks/does-not-exist" $token) $null

# Issues
$null = $report.AppendLine("## /issues")
$issueReq = @{ title="Legacy dependency"; description="Dependency needs upgrade"; projectId=$projectId; projectName=$projectName; severity="medium"; owner="Bob"; mitigationPlan="Upgrade"; dueDate="2026-02-10"; status="open" }
$issueCreate = Invoke-Json 'Post' "$base/issues" $issueReq $token
$issueId = $null; if ($issueCreate.status -in 200,201) { $issueId = ( $issueCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create issue (happy)" $issueCreate $issueReq
Add-Case $report "Create issue (missing required)" (Invoke-Json 'Post' "$base/issues" @{} $token) @{}
Add-Case $report "Create issue (invalid enum)" (Invoke-Json 'Post' "$base/issues" (@{ title="Bad"; description="Bad"; projectId=$projectId; projectName=$projectName; severity="bad"; owner="Bob"; mitigationPlan=""; dueDate="2026-02-10"; status="bad" }) $token) @{ severity="bad"; status="bad" }
Add-Case $report "List issues" (Invoke-Get "$base/issues" $token) $null
Add-Case $report "Get issue (happy)" (Invoke-Get "$base/issues/$issueId" $token) $null
Add-Case $report "Get issue (not found)" (Invoke-Get "$base/issues/does-not-exist" $token) $null
Add-Case $report "Update issue (happy)" (Invoke-Json 'Patch' "$base/issues/$issueId" @{ status="resolved" } $token) @{ status="resolved" }
Add-Case $report "Delete issue (invalid id)" (Invoke-Delete "$base/issues/does-not-exist" $token) $null
# Risks
$null = $report.AppendLine("## /risks")
$riskReq = @{ title="Scope creep"; description="Scope may increase"; projectId=$projectId; projectName=$projectName; probability=30; impact=60; owner="Claire"; mitigationPlan="Control"; dueDate="2026-02-15"; status="identified" }
$riskCreate = Invoke-Json 'Post' "$base/risks" $riskReq $token
$riskId = $null; if ($riskCreate.status -in 200,201) { $riskId = ( $riskCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create risk (happy)" $riskCreate $riskReq
Add-Case $report "Create risk (missing required)" (Invoke-Json 'Post' "$base/risks" @{} $token) @{}
Add-Case $report "Create risk (invalid enum)" (Invoke-Json 'Post' "$base/risks" (@{ title="Bad"; description="Bad"; projectId=$projectId; projectName=$projectName; probability=1; impact=1; owner="Claire"; mitigationPlan=""; dueDate="2026-02-15"; status="bad" }) $token) @{ status="bad" }
Add-Case $report "List risks" (Invoke-Get "$base/risks" $token) $null
Add-Case $report "Get risk (happy)" (Invoke-Get "$base/risks/$riskId" $token) $null
Add-Case $report "Get risk (not found)" (Invoke-Get "$base/risks/does-not-exist" $token) $null
Add-Case $report "Update risk (happy)" (Invoke-Json 'Patch' "$base/risks/$riskId" @{ status="mitigated" } $token) @{ status="mitigated" }
Add-Case $report "Delete risk (invalid id)" (Invoke-Delete "$base/risks/does-not-exist" $token) $null

# Milestones
$null = $report.AppendLine("## /milestones")
$milestoneReq = @{ name="Discovery Complete"; projectId=$projectId; projectName=$projectName; dueDate="2026-02-12"; deliverable="Discovery report"; acceptanceCriteria="Sign-off"; status="pending"; signOffBy="Lead PM" }
$milestoneCreate = Invoke-Json 'Post' "$base/milestones" $milestoneReq $token
$milestoneId = $null; if ($milestoneCreate.status -in 200,201) { $milestoneId = ( $milestoneCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create milestone (happy)" $milestoneCreate $milestoneReq
Add-Case $report "Create milestone (missing required)" (Invoke-Json 'Post' "$base/milestones" @{} $token) @{}
Add-Case $report "Create milestone (invalid enum)" (Invoke-Json 'Post' "$base/milestones" (@{ name="Bad"; projectId=$projectId; projectName=$projectName; dueDate="2026-02-12"; deliverable=""; acceptanceCriteria=""; status="bad"; signOffBy="" }) $token) @{ status="bad" }
Add-Case $report "List milestones" (Invoke-Get "$base/milestones" $token) $null
Add-Case $report "Get milestone (happy)" (Invoke-Get "$base/milestones/$milestoneId" $token) $null
Add-Case $report "Get milestone (not found)" (Invoke-Get "$base/milestones/does-not-exist" $token) $null
Add-Case $report "Update milestone (happy)" (Invoke-Json 'Patch' "$base/milestones/$milestoneId" @{ status="completed" } $token) @{ status="completed" }
Add-Case $report "Delete milestone (invalid id)" (Invoke-Delete "$base/milestones/does-not-exist" $token) $null

# Schedules
$null = $report.AppendLine("## /schedules")
$scheduleReq = @{ projectId=$projectId; projectName=$projectName; clientId=$clientId; clientName=$clientName; clientContactName="Jean Dupont"; clientContactEmail=$email; clientContactPhone="+33123456789"; scheduleStartDate="2026-02-05"; scheduleEndDate="2026-02-07"; scheduleColor="#FFAA00"; scheduleNotes="Initial"; isScheduled=$true; reminderEnabled=$true; createdBy="Scheduler" }
$scheduleCreate = Invoke-Json 'Post' "$base/schedules" $scheduleReq $token
$scheduleId = $null; if ($scheduleCreate.status -in 200,201) { $scheduleId = ( $scheduleCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create schedule (happy)" $scheduleCreate $scheduleReq
Add-Case $report "Create schedule (missing required)" (Invoke-Json 'Post' "$base/schedules" @{} $token) @{}
Add-Case $report "List schedules" (Invoke-Get "$base/schedules" $token) $null
Add-Case $report "Get schedule (happy)" (Invoke-Get "$base/schedules/$scheduleId" $token) $null
Add-Case $report "Get schedule (not found)" (Invoke-Get "$base/schedules/does-not-exist" $token) $null
Add-Case $report "Update schedule (happy)" (Invoke-Json 'Patch' "$base/schedules/$scheduleId" @{ scheduleNotes="Updated"; scheduleColor="#00AAFF" } $token) @{ scheduleNotes="Updated"; scheduleColor="#00AAFF" }
Add-Case $report "Delete schedule (invalid id)" (Invoke-Delete "$base/schedules/does-not-exist" $token) $null

# Reports + Dashboard
$null = $report.AppendLine("## /reports")
Add-Case $report "Reports summary" (Invoke-Get "$base/reports/summary" $token) $null
Add-Case $report "Reports revenue" (Invoke-Get "$base/reports/revenue" $token) $null
Add-Case $report "Reports pipeline" (Invoke-Get "$base/reports/pipeline" $token) $null

$null = $report.AppendLine("## /dashboard")
Add-Case $report "Dashboard overview" (Invoke-Get "$base/dashboard/overview" $token) $null
Add-Case $report "Dashboard stats" (Invoke-Get "$base/dashboard/stats" $token) $null

# Contacts + replies
$null = $report.AppendLine("## /contacts")
$contactReq = @{ name="Nadia"; email="nadia@example.com"; company="Nadia Co"; service="Consulting"; budget="5000"; message="Need estimate" }
$contactCreate = Invoke-Json 'Post' "$base/contacts" $contactReq $token
$contactId = $null; if ($contactCreate.status -in 200,201) { $contactId = ( $contactCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create contact (happy)" $contactCreate $contactReq
Add-Case $report "Create contact (missing required)" (Invoke-Json 'Post' "$base/contacts" @{} $token) @{}
Add-Case $report "List contacts" (Invoke-Get "$base/contacts" $token) $null
Add-Case $report "Get contact (happy)" (Invoke-Get "$base/contacts/$contactId" $token) $null
Add-Case $report "Update contact (happy)" (Invoke-Json 'Patch' "$base/contacts/$contactId" @{ status="in-progress"; isRead=$true } $token) @{ status="in-progress"; isRead=$true }
Add-Case $report "Create contact reply (happy)" (Invoke-Json 'Post' "$base/contacts/$contactId/replies" @{ message="Thanks" } $token) @{ message="Thanks" }
Add-Case $report "Create contact reply (missing required)" (Invoke-Json 'Post' "$base/contacts/$contactId/replies" @{} $token) @{}
Add-Case $report "Delete contact (invalid id)" (Invoke-Delete "$base/contacts/does-not-exist" $token) $null

# Invoices
$null = $report.AppendLine("## /invoices")
$invoiceReq = @{ invoiceNumber="INV-2026-001"; type="client"; projectId=$projectId; projectName=$projectName; clientId=$clientId; clientName=$clientName; clientAddress="10 Rue de Paris"; date="2026-02-04"; dueDate="2026-02-20"; amount=6400; currency="EUR"; status="sent"; notes="Net 14 days"; items=@(@{ description="Consulting days"; quantity=5; unitPrice=800; total=4000 }, @{ description="Workshops"; quantity=2; unitPrice=1200; total=2400 }) }
$invoiceCreate = Invoke-Json 'Post' "$base/invoices" $invoiceReq $token
$invoiceId = $null; if ($invoiceCreate.status -in 200,201) { $invoiceId = ( $invoiceCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create invoice (happy)" $invoiceCreate $invoiceReq
Add-Case $report "Create invoice (missing required)" (Invoke-Json 'Post' "$base/invoices" @{} $token) @{}
Add-Case $report "Create invoice (invalid enum)" (Invoke-Json 'Post' "$base/invoices" (@{ invoiceNumber="INV-X"; type="client"; projectId=$projectId; projectName=$projectName; clientId=$clientId; clientName=$clientName; clientAddress=""; date="2026-02-04"; dueDate="2026-02-20"; amount=1; currency="EUR"; status="bad"; notes=""; items=@() }) $token) @{ status="bad" }
Add-Case $report "List invoices" (Invoke-Get "$base/invoices" $token) $null
Add-Case $report "Get invoice (happy)" (Invoke-Get "$base/invoices/$invoiceId" $token) $null
Add-Case $report "Get invoice (not found)" (Invoke-Get "$base/invoices/does-not-exist" $token) $null
Add-Case $report "Update invoice (happy)" (Invoke-Json 'Patch' "$base/invoices/$invoiceId" @{ notes="Updated" } $token) @{ notes="Updated" }
Add-Case $report "Update invoice status (happy)" (Invoke-Json 'Post' "$base/invoices/$invoiceId/status" @{ status="paid" } $token) @{ status="paid" }
Add-Case $report "Update invoice status (invalid enum)" (Invoke-Json 'Post' "$base/invoices/$invoiceId/status" @{ status="bad" } $token) @{ status="bad" }
Add-Case $report "Delete invoice (invalid id)" (Invoke-Delete "$base/invoices/does-not-exist" $token) $null

# Documents (metadata + upload)
$null = $report.AppendLine("## /documents")
$docMetaReq = @{ name="contract.pdf"; category="contract"; clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; uploadedBy="System"; uploadedAt="2026-02-05"; size="12KB"; fileType="PDF" }
$docMetaCreate = Invoke-Json 'Post' "$base/documents" $docMetaReq $token
$docMetaId = $null; if ($docMetaCreate.status -in 200,201) { $docMetaId = ( $docMetaCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create document metadata (happy)" $docMetaCreate $docMetaReq
Add-Case $report "Create document metadata (missing required)" (Invoke-Json 'Post' "$base/documents" @{} $token) @{}
Add-Case $report "Create document metadata (invalid enum)" (Invoke-Json 'Post' "$base/documents" (@{ name="bad"; category="bad"; clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; uploadedBy="System" }) $token) @{ category="bad" }
Add-Case $report "List documents" (Invoke-Get "$base/documents" $token) $null
Add-Case $report "Get document (happy)" (Invoke-Get "$base/documents/$docMetaId" $token) $null
Add-Case $report "Get document (not found)" (Invoke-Get "$base/documents/does-not-exist" $token) $null
Add-Case $report "Download document (not found)" (Invoke-Get "$base/documents/does-not-exist/download" $token) $null

$filePath = "C:\Users\arebh\Desktop\New folder\1600w-a1RYzvS1EFo.webp"
if (Test-Path $filePath) {
  $uploadFields = @{ name="1600w-a1RYzvS1EFo.webp"; category="contract"; clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; uploadedBy="System"; uploadedAt="2026-02-05"; size="1600 KB"; fileType="WEBP" }
  $docUpload = Invoke-MultipartUpload "$base/documents" $filePath $uploadFields $token
  $docUploadId = $null
  if ($docUpload.status -in 200,201) { $docUploadId = ( $docUpload.body | ConvertFrom-Json ).data.id }
  Add-Case $report "Upload document (happy)" $docUpload $uploadFields
  Add-Case $report "Download uploaded document" (Invoke-Get "$base/documents/$docUploadId/download" $token) $null
  Add-Case $report "Delete uploaded document" (Invoke-Delete "$base/documents/$docUploadId" $token) $null
}
Add-Case $report "Upload document (missing file)" (Invoke-Json 'Post' "$base/documents" (@{ category="contract"; uploadedBy="System" }) $token) @{ category="contract"; uploadedBy="System" }

# OpenAPI docs
$null = $report.AppendLine("## /openapi")
Add-Case $report "OpenAPI docs" (Invoke-Get "http://localhost:8081/v3/api-docs" $null) $null

# Write report
$docsDir = Join-Path (Get-Location) "docs"
if (-not (Test-Path $docsDir)) { New-Item -ItemType Directory -Path $docsDir | Out-Null }
$reportPath = Join-Path $docsDir "api-report.md"
[System.IO.File]::WriteAllText($reportPath, $report.ToString())
"REPORT_WRITTEN $reportPath"
