# Backend Test Report

Date: 2026-02-04  
Project: `consultant-business-api`  
Environment: Windows 11, Java 21, MongoDB Atlas (`legy_test`), local server at `http://localhost:8080`

## Summary
- Unit tests: PASS (77 run, 1 skipped)
- Integration tests: PASS (21 run)
- Contract tests (Schemathesis, coverage phase): PASS with warnings
- Performance tests (k6): PASS (smoke + load), thresholds met

## Unit + Integration Tests
Command:
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
$env:MONGODB_URI_TEST="mongodb+srv://youssef:***@adminfacter.r0rwc.mongodb.net/legy_test?retryWrites=true&w=majority"
.\mvnw.cmd verify
```
Results:
- Unit tests: 77 run, 0 failures, 0 errors, 1 skipped
- Integration tests: 21 run, 0 failures, 0 errors
- Build: SUCCESS
- Notes: Mockito emitted standard dynamic-agent warnings (non-failing)

## Contract Tests (Schemathesis)
Command:
```powershell
tests\contract\run_schemathesis.ps1 -BaseUrl http://localhost:8080 -MaxExamples 30
```
Results:
- Coverage phase: 88/88 operations passed
- Warnings:
  - 5 endpoints returned 401 (user self-service endpoints expect auth)
  - 40 endpoints returned 404 due to missing seeded IDs
  - Schema vs validation mismatch on `POST /clients`, `POST /contacts`, `POST /invoices` (API validation stricter than schema)

## Performance Tests (k6)
### Smoke
Command:
```powershell
& "C:\Program Files\k6\k6.exe" run tests\perf\k6\smoke.js
```
Key metrics:
- Checks: 100% passed (90/90)
- http_req_failed: 0.00%
- http_req_duration p95: 371.79ms

### Load
Command:
```powershell
& "C:\Program Files\k6\k6.exe" run tests\perf\k6\load.js
```
Thresholds:
- `http_req_failed < 1%`: PASS (0.00%)
- `p(95) < 800ms`: PASS (323.58ms)

Key metrics:
- Checks: 100% passed (2037/2037)
- http_req_duration p95: 323.58ms

## Notes / Known Warnings
- Contract tests report missing seeded data for many `{id}` endpoints; to remove these warnings, provide seeded IDs or a Schemathesis config with stable fixtures.
- User self-service endpoints require auth; contract tests intentionally ran without credentials.
