param(
    [string]$BaseUrl = "http://localhost:8080",
    [int]$MaxExamples = 50
)

if (-not (Get-Command schemathesis -ErrorAction SilentlyContinue)) {
    Write-Error "schemathesis is not installed. Run: pip install -r tests/contract/requirements.txt"
    exit 1
}

$env:PYTHONIOENCODING = "utf-8"
$env:PYTHONUTF8 = "1"

schemathesis run "$BaseUrl/v3/api-docs" `
    -c all `
    --exclude-checks positive_data_acceptance,negative_data_rejection,ignored_auth `
    -m positive `
    --phases coverage `
    -n $MaxExamples `
    --exclude-path-regex "/api(/v1)?/auth.*"
