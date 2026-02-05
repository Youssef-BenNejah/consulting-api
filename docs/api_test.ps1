$ErrorActionPreference = 'Stop'
$base = "http://localhost:8081/api/v1"
$now = Get-Date

function Invoke-Json($method, $url, $body, $token) {
  $method = $method.ToUpperInvariant()
  $tmp = [System.IO.Path]::GetTempFileName()
  $bodyJson = if ($null -ne $body) { $body | ConvertTo-Json -Depth 10 } else { "" }
  Set-Content -Path $tmp -Value $bodyJson -Encoding UTF8
  $args = @('-s', '-X', $method, '-H', 'Content-Type: application/json')
  if ($token) { $args += @('-H', "Authorization: Bearer $token") }
  if ($method -ne 'GET' -and $method -ne 'DELETE') { $args += @('--data-binary', "@$tmp") }
  $args += @($url, '-w', "`n%{http_code}")
  $out = & curl.exe @args
  Remove-Item $tmp -Force
  $parts = $out -split "`n"
  $code = $parts[-1]
  $bodyText = if ($parts.Length -gt 1) { ($parts[0..($parts.Length-2)] -join "`n") } else { "" }
  return @{ ok=([int]$code -ge 200 -and [int]$code -lt 300); status=[int]$code; body=$bodyText }
}

function Invoke-Get($url, $token) {
  $args = @('-s', '-X', 'GET')
  if ($token) { $args += @('-H', "Authorization: Bearer $token") }
  $args += @($url, '-w', "`n%{http_code}")
  $out = & curl.exe @args
  $parts = $out -split "`n"
  $code = $parts[-1]
  $bodyText = if ($parts.Length -gt 1) { ($parts[0..($parts.Length-2)] -join "`n") } else { "" }
  return @{ ok=([int]$code -ge 200 -and [int]$code -lt 300); status=[int]$code; body=$bodyText }
}

function Invoke-GetBinary($url, $token) {
  $tmp = [System.IO.Path]::GetTempFileName()
  $hdr = [System.IO.Path]::GetTempFileName()
  $args = @('-s', '-D', $hdr, '-o', $tmp, '-X', 'GET')
  if ($token) { $args += @('-H', "Authorization: Bearer $token") }
  $args += @($url, '-w', "`n%{http_code}")
  $out = & curl.exe @args
  $code = [int]($out -split "`n")[-1]
  $len = (Get-Item $tmp).Length
  $ct = (Get-Content $hdr | Where-Object { $_ -match '^Content-Type:' } | Select-Object -First 1)
  if ($ct) { $ct = $ct.Substring(13).Trim() } else { $ct = "" }
  Remove-Item $tmp -Force
  Remove-Item $hdr -Force
  $bodyText = "{`"binary_bytes`": $len, `"content_type`": `"$ct`"}"
  return @{ ok=($code -ge 200 -and $code -lt 300); status=$code; body=$bodyText }
}

function Invoke-Delete($url, $token) {
  $args = @('-s', '-X', 'DELETE')
  if ($token) { $args += @('-H', "Authorization: Bearer $token") }
  $args += @($url, '-w', "`n%{http_code}")
  $out = & curl.exe @args
  $parts = $out -split "`n"
  $code = $parts[-1]
  $bodyText = if ($parts.Length -gt 1) { ($parts[0..($parts.Length-2)] -join "`n") } else { "" }
  return @{ ok=([int]$code -ge 200 -and [int]$code -lt 300); status=[int]$code; body=$bodyText }
}

function Invoke-MultipartUpload($url, $filePath, $fields, $token) {
  $args = @('-s', '-X', 'POST')
  if ($token) { $args += @('-H', "Authorization: Bearer $token") }
  $args += @('-F', "file=@$filePath;type=image/webp")
  foreach ($k in $fields.Keys) { $args += @('-F', "${k}=$($fields[$k])") }
  $args += @($url, '-w', "`n%{http_code}")
  $out = & curl.exe @args
  $parts = $out -split "`n"
  $code = $parts[-1]
  $bodyText = if ($parts.Length -gt 1) { ($parts[0..($parts.Length-2)] -join "`n") } else { "" }
  return @{ ok=([int]$code -ge 200 -and [int]$code -lt 300); status=[int]$code; body=$bodyText }
}

function Get-ExpectedStatus($title, $override) {
  if ($override) { return [int]$override }
  $t = $title.ToLowerInvariant()
  if ($t -like "*not found*") { return 404 }
  if ($t -like "*invalid id*") { return 404 }
  if ($t -like "*missing required*") { return 400 }
  if ($t -like "*missing file*") { return 400 }
  if ($t -like "*invalid enum*") { return 400 }
  if ($t -like "*invalid*") { return 400 }
  if ($t -like "*invalid page*") { return 200 }
  if ($t -like "register*") { return 201 }
  if ($t -like "login*") { return 200 }
  if ($t -like "create*") { return 201 }
  if ($t -like "upload*") { return 201 }
  if ($t -like "update invoice status*") { return 200 }
  if ($t -like "update*") { return 200 }
  if ($t -like "delete uploaded*") { return 204 }
  if ($t -like "delete*") { return 204 }
  if ($t -like "download*") { return 200 }
  if ($t -like "list*") { return 200 }
  if ($t -like "get*") { return 200 }
  if ($t -like "openapi*") { return 200 }
  return 200
}

function Add-Case($sb, $title, $resp, $request, $expectedStatus) {
  $expected = Get-ExpectedStatus $title $expectedStatus
  $null = $sb.AppendLine("### $title")
  if ($request -ne $null) {
    $null = $sb.AppendLine("Request:")
    $null = $sb.AppendLine('```json')
    $null = $sb.AppendLine(($request | ConvertTo-Json -Depth 10))
    $null = $sb.AppendLine('```')
  }
  $null = $sb.AppendLine("Expected status=$expected")
  $null = $sb.AppendLine("Response: status=$($resp.status)")
  $null = $sb.AppendLine('```json')
  $null = $sb.AppendLine(($resp.body))
  $null = $sb.AppendLine('```')
  $null = $sb.AppendLine("")
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
$lastReg = $null
$lastLogin = $null
for ($i=1; $i -le 10; $i++) {
  $ts = Get-Date -Format "yyyyMMddHHmmssfff"
  $email = "test$ts@example.com"
  $phone = "+21699$((Get-Random -Minimum 100000 -Maximum 999999))"
  $registerBody = @{ firstName="Test"; lastName="User"; email=$email; phoneNumber=$phone; password=$password; confirmPassword=$password }
  $lastReg = Invoke-Json 'POST' "$base/auth/register" $registerBody $null
  if ($lastReg.status -eq 201 -or $lastReg.status -eq 200) {
    $loginBody = @{ email=$email; password=$password }
    $lastLogin = Invoke-Json 'POST' "$base/auth/login" $loginBody $null
    if ($lastLogin.status -eq 200) {
      $token = ( $lastLogin.body | ConvertFrom-Json ).access_token
      break
    }
  }
}

$null = $report.AppendLine("## Auth")
$null = $report.AppendLine("- Registered email: $email")
$null = $report.AppendLine("- Registered phone: $phone")
$null = $report.AppendLine("- Login token acquired: $([bool]$token)")
$null = $report.AppendLine("")
if ($lastReg -ne $null) { Add-Case $report "Register (last attempt)" $lastReg $registerBody 201 }
if ($lastLogin -ne $null) { Add-Case $report "Login (last attempt)" $lastLogin @{ email=$email; password=$password } 200 }
if (-not $token) { throw "Auth failed after 10 attempts. Aborting endpoint tests." }

# Seed base data
$clientReq = @{ name="Client $($now.ToString("yyyyMMddHHmmss"))"; industry="IT"; country="US"; primaryContact="Test User"; email=$email; phone="12025550123"; contractType="Enterprise"; notes="Created by API"; tags=@("tag1","tag2") }
$clientCreate = Invoke-Json 'Post' "$base/clients" $clientReq $token
$clientId = $null; $clientName = $null
if ($clientCreate.status -in 200,201) { $clientId = ( $clientCreate.body | ConvertFrom-Json ).data.id; $clientName = ( $clientCreate.body | ConvertFrom-Json ).data.name }
if (-not $clientId) { throw "Client creation failed. Aborting endpoint tests." }

$projectReq = @{ projectId="PRJ-$($now.ToString("yyyyMMddHHmmss"))"; clientId=$clientId; clientName=$clientName; name="Project $($now.ToString("yyyyMMddHHmmss"))"; description="Test project"; startDate="2026-02-05"; endDate="2026-03-05"; status="delivery"; type="fixed"; clientBudget=120000; vendorCost=40000; internalCost=30000; healthStatus="green"; progress=20 }
$projectCreate = Invoke-Json 'Post' "$base/projects" $projectReq $token
$projectId = $null; $projectName = $null
if ($projectCreate.status -in 200,201) { $projectId = ( $projectCreate.body | ConvertFrom-Json ).data.id; $projectName = ( $projectCreate.body | ConvertFrom-Json ).data.name }
if (-not $projectId) { throw "Project creation failed. Aborting endpoint tests." }

# Clients
$null = $report.AppendLine("## /clients")
Add-Case $report "Create client (happy)" $clientCreate $clientReq 201
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
Add-Case $report "Create project (happy)" $projectCreate $projectReq 201
Add-Case $report "Create project (missing required)" (Invoke-Json 'Post' "$base/projects" @{} $token) @{}
Add-Case $report "Create project (invalid enum)" (Invoke-Json 'Post' "$base/projects" (@{ projectId="PRJ-X"; clientId=$clientId; clientName=$clientName; name="Bad"; description="Bad"; startDate="2026-02-05"; endDate="2026-03-05"; status="bad"; type="fixed"; clientBudget=1; vendorCost=1; internalCost=1; healthStatus="green"; progress=0 }) $token) @{ status="bad" }
Add-Case $report "List projects (happy)" (Invoke-Get "$base/projects" $token) $null
Add-Case $report "Get project (happy)" (Invoke-Get "$base/projects/$projectId" $token) $null
Add-Case $report "Get project (not found)" (Invoke-Get "$base/projects/does-not-exist" $token) $null
Add-Case $report "Update project (happy)" (Invoke-Json 'Patch' "$base/projects/$projectId" @{ progress=40; healthStatus="amber"; status="review" } $token) @{ progress=40; healthStatus="amber"; status="review" }
Add-Case $report "Update project (invalid)" (Invoke-Json 'Patch' "$base/projects/$projectId" @{ progress=200 } $token) @{ progress=200 }
Add-Case $report "Delete project (invalid id)" (Invoke-Delete "$base/projects/does-not-exist" $token) $null

# Document categories
$null = $report.AppendLine("## /document-categories")
$docCatReq = @{ name="Contract"; key="contract-$($now.ToString("HHmmss"))"; color="#0055FF" }
$docCatCreate = Invoke-Json 'Post' "$base/document-categories" $docCatReq $token
$docCatId = $null; if ($docCatCreate.status -in 200,201) { $docCatId = ( $docCatCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create document category (happy)" $docCatCreate $docCatReq 201
Add-Case $report "Create document category (missing required)" (Invoke-Json 'Post' "$base/document-categories" @{} $token) @{}
Add-Case $report "List document categories" (Invoke-Get "$base/document-categories" $token) $null
Add-Case $report "Get document category (happy)" (Invoke-Get "$base/document-categories/$docCatId" $token) $null
Add-Case $report "Get document category (not found)" (Invoke-Get "$base/document-categories/does-not-exist" $token) $null
Add-Case $report "Update document category (happy)" (Invoke-Json 'Patch' "$base/document-categories/$docCatId" @{ color="#000000" } $token) @{ color="#000000" }
Add-Case $report "Delete document category (invalid id)" (Invoke-Delete "$base/document-categories/does-not-exist" $token) $null

# Project types
$null = $report.AppendLine("## /project-types")
$projTypeReq = @{ name="Fixed Price"; key="fixed-$($now.ToString("HHmmss"))"; description="Fixed scope" }
$projTypeCreate = Invoke-Json 'Post' "$base/project-types" $projTypeReq $token
$projTypeId = $null; if ($projTypeCreate.status -in 200,201) { $projTypeId = ( $projTypeCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create project type (happy)" $projTypeCreate $projTypeReq 201
Add-Case $report "Create project type (missing required)" (Invoke-Json 'Post' "$base/project-types" @{} $token) @{}
Add-Case $report "List project types" (Invoke-Get "$base/project-types" $token) $null
Add-Case $report "Get project type (happy)" (Invoke-Get "$base/project-types/$projTypeId" $token) $null
Add-Case $report "Get project type (not found)" (Invoke-Get "$base/project-types/does-not-exist" $token) $null
Add-Case $report "Update project type (happy)" (Invoke-Json 'Patch' "$base/project-types/$projTypeId" @{ description="Updated" } $token) @{ description="Updated" }
Add-Case $report "Delete project type (invalid id)" (Invoke-Delete "$base/project-types/does-not-exist" $token) $null

# Contract types
$null = $report.AppendLine("## /contract-types")
$contractTypeReq = @{ name="Enterprise"; key="enterprise-$($now.ToString("HHmmss"))"; description="Enterprise agreement" }
$contractTypeCreate = Invoke-Json 'Post' "$base/contract-types" $contractTypeReq $token
$contractTypeId = $null; if ($contractTypeCreate.status -in 200,201) { $contractTypeId = ( $contractTypeCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create contract type (happy)" $contractTypeCreate $contractTypeReq 201
Add-Case $report "Create contract type (missing required)" (Invoke-Json 'Post' "$base/contract-types" @{} $token) @{}
Add-Case $report "List contract types" (Invoke-Get "$base/contract-types" $token) $null
Add-Case $report "Get contract type (happy)" (Invoke-Get "$base/contract-types/$contractTypeId" $token) $null
Add-Case $report "Get contract type (not found)" (Invoke-Get "$base/contract-types/does-not-exist" $token) $null
Add-Case $report "Update contract type (happy)" (Invoke-Json 'Patch' "$base/contract-types/$contractTypeId" @{ description="Updated" } $token) @{ description="Updated" }
Add-Case $report "Delete contract type (invalid id)" (Invoke-Delete "$base/contract-types/does-not-exist" $token) $null

# Communication logs
$null = $report.AppendLine("## /communication-logs")
$commReq = @{ clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; date="2026-02-05"; type="meeting"; summary="Weekly sync"; actionItems=@("Send summary"); participants=@("Alice","Bob") }
$commCreate = Invoke-Json 'Post' "$base/communication-logs" $commReq $token
$commId = $null; if ($commCreate.status -in 200,201) { $commId = ( $commCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create communication log (happy)" $commCreate $commReq 201
Add-Case $report "Create communication log (missing required)" (Invoke-Json 'Post' "$base/communication-logs" @{} $token) @{}
Add-Case $report "Create communication log (invalid enum)" (Invoke-Json 'Post' "$base/communication-logs" (@{ clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; date="2026-02-05"; type="bad"; summary="Bad"; actionItems=@(); participants=@() }) $token) @{ type="bad" }
Add-Case $report "List communication logs" (Invoke-Get "$base/communication-logs" $token) $null
Add-Case $report "Get communication log (happy)" (Invoke-Get "$base/communication-logs/$commId" $token) $null
Add-Case $report "Get communication log (not found)" (Invoke-Get "$base/communication-logs/does-not-exist" $token) $null
Add-Case $report "Update communication log (happy)" (Invoke-Json 'Patch' "$base/communication-logs/$commId" @{ summary="Updated" } $token) @{ summary="Updated" }
Add-Case $report "Delete communication log (invalid id)" (Invoke-Delete "$base/communication-logs/does-not-exist" $token) $null

# Schedules
$null = $report.AppendLine("## /schedules")
$scheduleReq = @{ projectId=$projectId; projectName=$projectName; clientId=$clientId; clientName=$clientName; clientContactName="Jean Dupont"; clientContactEmail=$email; clientContactPhone="+33123456789"; scheduleStartDate="2026-02-05"; scheduleEndDate="2026-02-07"; scheduleColor="#FFAA00"; scheduleNotes="Initial"; isScheduled=$true; reminderEnabled=$true; createdBy="Scheduler" }
$scheduleCreate = Invoke-Json 'Post' "$base/schedules" $scheduleReq $token
$scheduleId = $null; if ($scheduleCreate.status -in 200,201) { $scheduleId = ( $scheduleCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create schedule (happy)" $scheduleCreate $scheduleReq 201
Add-Case $report "Create schedule (missing required)" (Invoke-Json 'Post' "$base/schedules" @{} $token) @{}
Add-Case $report "List schedules" (Invoke-Get "$base/schedules" $token) $null
Add-Case $report "Get schedule (happy)" (Invoke-Get "$base/schedules/$scheduleId" $token) $null
Add-Case $report "Get schedule (not found)" (Invoke-Get "$base/schedules/does-not-exist" $token) $null
Add-Case $report "Update schedule (happy)" (Invoke-Json 'Patch' "$base/schedules/$scheduleId" @{ scheduleNotes="Updated"; scheduleColor="#00AAFF" } $token) @{ scheduleNotes="Updated"; scheduleColor="#00AAFF" }
Add-Case $report "Delete schedule (invalid id)" (Invoke-Delete "$base/schedules/does-not-exist" $token) $null

# Reports + Dashboard
$null = $report.AppendLine("## /reports")
Add-Case $report "Reports overview" (Invoke-Get "$base/reports/overview" $token) $null 200
Add-Case $report "Reports projects" (Invoke-Get "$base/reports/projects" $token) $null 200
Add-Case $report "Reports margins" (Invoke-Get "$base/reports/margins" $token) $null 200
Add-Case $report "Reports invoices" (Invoke-Get "$base/reports/invoices" $token) $null 200

$null = $report.AppendLine("## /dashboard")
Add-Case $report "Dashboard summary" (Invoke-Get "$base/dashboard/summary" $token) $null 200
Add-Case $report "Dashboard notifications" (Invoke-Get "$base/dashboard/notifications" $token) $null 200

# Contacts + replies
$null = $report.AppendLine("## /contacts")
$contactReq = @{ name="Nadia"; email="nadia@example.com"; company="Nadia Co"; service="Consulting"; budget="5000"; message="Need estimate" }
$contactCreate = Invoke-Json 'Post' "$base/contacts" $contactReq $token
$contactId = $null; if ($contactCreate.status -in 200,201) { $contactId = ( $contactCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create contact (happy)" $contactCreate $contactReq 201
Add-Case $report "Create contact (missing required)" (Invoke-Json 'Post' "$base/contacts" @{} $token) @{}
Add-Case $report "List contacts" (Invoke-Get "$base/contacts" $token) $null
Add-Case $report "Get contact (happy)" (Invoke-Get "$base/contacts/$contactId" $token) $null
Add-Case $report "Update contact (happy)" (Invoke-Json 'Patch' "$base/contacts/$contactId" @{ status="in-progress"; isRead=$true } $token) @{ status="in-progress"; isRead=$true }
Add-Case $report "Create contact reply (happy)" (Invoke-Json 'Post' "$base/contacts/$contactId/replies" @{ message="Thanks" } $token) @{ message="Thanks" } 201
Add-Case $report "Create contact reply (missing required)" (Invoke-Json 'Post' "$base/contacts/$contactId/replies" @{} $token) @{}
Add-Case $report "Delete contact (invalid id)" (Invoke-Delete "$base/contacts/does-not-exist" $token) $null

# Invoices
$null = $report.AppendLine("## /invoices")
$invoiceReq = @{ invoiceNumber="INV-2026-001"; type="client"; projectId=$projectId; projectName=$projectName; clientId=$clientId; clientName=$clientName; clientAddress="10 Rue de Paris"; date="2026-02-04"; dueDate="2026-02-20"; amount=6400; currency="EUR"; status="sent"; notes="Net 14 days"; items=@(@{ description="Consulting days"; quantity=5; unitPrice=800; total=4000 }, @{ description="Workshops"; quantity=2; unitPrice=1200; total=2400 }) }
$invoiceCreate = Invoke-Json 'Post' "$base/invoices" $invoiceReq $token
$invoiceId = $null; if ($invoiceCreate.status -in 200,201) { $invoiceId = ( $invoiceCreate.body | ConvertFrom-Json ).data.id }
Add-Case $report "Create invoice (happy)" $invoiceCreate $invoiceReq 201
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
Add-Case $report "Create document metadata (happy)" $docMetaCreate $docMetaReq 201
Add-Case $report "Create document metadata (missing required)" (Invoke-Json 'Post' "$base/documents" @{} $token) @{}
Add-Case $report "Create document metadata (invalid enum)" (Invoke-Json 'Post' "$base/documents" (@{ name="bad"; category="bad"; clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; uploadedBy="System" }) $token) @{ category="bad" }
Add-Case $report "List documents" (Invoke-Get "$base/documents" $token) $null
Add-Case $report "Get document (happy)" (Invoke-Get "$base/documents/$docMetaId" $token) $null
Add-Case $report "Get document (not found)" (Invoke-Get "$base/documents/does-not-exist" $token) $null
Add-Case $report "Download document (not found)" (Invoke-GetBinary "$base/documents/does-not-exist/download" $token) $null

$filePath = "C:\Users\arebh\Desktop\New folder\1600w-a1RYzvS1EFo.webp"
if (Test-Path $filePath) {
  $uploadFields = @{ name="1600w-a1RYzvS1EFo.webp"; category="contract"; clientId=$clientId; clientName=$clientName; projectId=$projectId; projectName=$projectName; uploadedBy="System"; uploadedAt="2026-02-05"; size="1600 KB"; fileType="WEBP" }
  $docUpload = Invoke-MultipartUpload "$base/documents" $filePath $uploadFields $token
  $docUploadId = $null
  if ($docUpload.status -in 200,201) { $docUploadId = ( $docUpload.body | ConvertFrom-Json ).data.id }
  Add-Case $report "Upload document (happy)" $docUpload $uploadFields 201
  Add-Case $report "Download uploaded document" (Invoke-GetBinary "$base/documents/$docUploadId/download" $token) $null 200
  Add-Case $report "Delete uploaded document" (Invoke-Delete "$base/documents/$docUploadId" $token) $null 204
}
Add-Case $report "Upload document (missing file)" (Invoke-Json 'Post' "$base/documents" (@{ category="contract"; uploadedBy="System" }) $token) @{ category="contract"; uploadedBy="System" }

# OpenAPI docs
$null = $report.AppendLine("## /openapi")
$openapiResp = Invoke-Get "http://localhost:8081/v3/api-docs" $null
$openapiLen = if ($openapiResp.body) { $openapiResp.body.Length } else { 0 }
$openapiResp.body = "{`"length`": $openapiLen}"
Add-Case $report "OpenAPI docs" $openapiResp $null 200

# Summary builders
function Get-CaseStatusCodes($lines) {
  $codes = @()
  $expected = @()
  $i = 0
  while ($i -lt $lines.Length) {
    if ($lines[$i] -like "### *") {
      $j = $i + 1
      $codeFound = $false
      $expFound = $false
      $exp = 0
      $code = 0
      while ($j -lt $lines.Length -and -not ($lines[$j] -like "### *")) {
        if ($lines[$j] -match '^Expected status=(\d+)$') { $exp = [int]$matches[1]; $expFound = $true }
        if ($lines[$j] -match '^Response: status=(\d+)$') { $codes += [int]$matches[1]; $codeFound = $true; break }
        $j++
      }
      if (-not $codeFound) { $codes += 0 }
      if (-not $expFound) { $expected += 0 } else { $expected += $exp }
      $i = $j
    } else { $i++ }
  }
  return @{ actual=$codes; expected=$expected }
}

$content = $report.ToString()
$sections = [System.Text.RegularExpressions.Regex]::Split($content, "\r?\n## ") | Select-Object -Skip 1
$summary = New-Object System.Text.StringBuilder
$null = $summary.AppendLine("## Summary (Detailed)")
$null = $summary.AppendLine("")
$successRate = New-Object System.Text.StringBuilder
$null = $successRate.AppendLine("## Success Rate (Per Section)")
$null = $successRate.AppendLine("")

foreach ($sec in $sections) {
  $lines = [System.Text.RegularExpressions.Regex]::Split($sec, "\r?\n")
  $title = $lines[0].Trim()
  $cases = ($lines | Select-String -Pattern '^### ')
  if ($cases.Count -eq 0) { continue }
  $pair = Get-CaseStatusCodes $lines
  $statusCodes = $pair.actual
  $expectedCodes = $pair.expected
  $total = $statusCodes.Count
  $success = 0
  for ($i = 0; $i -lt $total; $i++) {
    if ($expectedCodes[$i] -ne 0 -and $statusCodes[$i] -eq $expectedCodes[$i]) { $success++ }
  }
  $errCount = $total - $success
  $codeGroups = $statusCodes | Group-Object | Sort-Object Name | ForEach-Object { "{0}x{1}" -f $_.Name, $_.Count }

  $null = $summary.AppendLine("- ${title}")
  $null = $summary.AppendLine("  Total cases: $total")
  $null = $summary.AppendLine("  Success: $success")
  $null = $summary.AppendLine("  Error: $errCount")
  $null = $summary.AppendLine("  Status codes: $([string]::Join(', ', $codeGroups))")
  $null = $summary.AppendLine("")

  $rate = if ($total -gt 0) { [Math]::Round(($success / $total) * 100, 2) } else { 0 }
  $null = $successRate.AppendLine("- ${title}: $rate% ($success/$total)")
}

$null = $report.AppendLine("")
$null = $report.AppendLine($summary.ToString())
$null = $report.AppendLine($successRate.ToString())

# Write report
$docsDir = Join-Path (Get-Location) "docs"
if (-not (Test-Path $docsDir)) { New-Item -ItemType Directory -Path $docsDir | Out-Null }
$reportPath = Join-Path $docsDir "api-report.md"
[System.IO.File]::WriteAllText($reportPath, $report.ToString())
"REPORT_WRITTEN $reportPath"
