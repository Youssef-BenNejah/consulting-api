# Testing Guide

## Unit Tests

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd test
```

## Integration Tests (Atlas)

These tests connect to MongoDB Atlas. The test profile refuses to run unless the database name contains `test`.

```powershell
$env:MONGODB_URI_TEST="mongodb+srv://<user>:<pass>@<cluster>/<db>_test?retryWrites=true&w=majority"
.\mvnw.cmd verify
```

## Contract Tests (OpenAPI + Schemathesis)

Start the API locally, then run:

```powershell
pip install -r tests/contract/requirements.txt
powershell -ExecutionPolicy Bypass -File tests/contract/run_schemathesis.ps1 -BaseUrl http://localhost:8080
```

## Performance Tests (k6)

Start the API locally, then run:

```powershell
k6 run tests/perf/k6/smoke.js
k6 run tests/perf/k6/load.js
```
