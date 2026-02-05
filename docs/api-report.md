# API Endpoint Test Report

- Generated: 2026-02-05 09:18:01
- Base URL: http://localhost:8081/api/v1

## Auth
- Registered email: test20260205091803285@example.com
- Registered phone: +21699483236
- Login token acquired: False

## /clients
### Create client (happy)
Request:
```json
{
    "contractType":  "Enterprise",
    "industry":  "IT",
    "email":  "test20260205091803285@example.com",
    "notes":  "Created by API",
    "name":  "Client ",
    "tags":  [
                 "tag1",
                 "tag2"
             ],
    "country":  "US",
    "phone":  "12025550123",
    "primaryContact":  "Test User"
}
```
Response: status=
```json
Object reference not set to an instance of an object.
```

### Create client (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be blank","message":"must not be blank","field":"industry"},{"code":"must not be blank","message":"must not be blank","field":"contractType"},{"code":"must not be blank","message":"must not be blank","field":"notes"},{"code":"must not be blank","message":"must not be blank","field":"phone"},{"code":"must not be blank","message":"must not be blank","field":"primaryContact"},{"code":"must not be null","message":"must not be null","field":"tags"},{"code":"must not be blank","message":"must not be blank","field":"country"},{"code":"must not be blank","message":"must not be blank","field":"email"}]}
```

### List clients (happy)
Response: status=
```json
Object reference not set to an instance of an object.
```

### List clients (invalid page)
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get client (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get client (not found)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Client not found: does-not-exist"}]}
```

### Update client (happy)
Request:
```json
{
    "notes":  "Updated",
    "industry":  "FinTech"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update client (invalid)
Request:
```json
{

}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete client (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Client not found: does-not-exist"}]}
```

## /projects
### Create project (happy)
Request:
```json
{
    "openTasks":  5,
    "progress":  20,
    "status":  "delivery",
    "clientBudget":  120000,
    "type":  "fixed",
    "startDate":  "2026-02-05",
    "clientName":  null,
    "openRisks":  1,
    "openIssues":  1,
    "description":  "Test project",
    "healthStatus":  "green",
    "clientId":  null,
    "endDate":  "2026-03-05",
    "projectId":  "PRJ-",
    "internalCost":  30000,
    "name":  "Project ",
    "vendorCost":  40000
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be blank","message":"must not be blank","field":"clientId"}]}
```

### Create project (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be null","message":"must not be null","field":"openIssues"},{"code":"must not be null","message":"must not be null","field":"vendorCost"},{"code":"must not be null","message":"must not be null","field":"healthStatus"},{"code":"must not be null","message":"must not be null","field":"openRisks"},{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be null","message":"must not be null","field":"openTasks"},{"code":"must not be null","message":"must not be null","field":"endDate"},{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be null","message":"must not be null","field":"clientBudget"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be null","message":"must not be null","field":"internalCost"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be null","message":"must not be null","field":"progress"},{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be null","message":"must not be null","field":"type"},{"code":"must not be null","message":"must not be null","field":"startDate"}]}
```

### Create project (invalid enum)
Request:
```json
{
    "status":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List projects (happy)
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get project (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get project (not found)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Project not found: does-not-exist"}]}
```

### Update project (happy)
Request:
```json
{
    "status":  "review",
    "progress":  40,
    "healthStatus":  "amber"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update project (invalid)
Request:
```json
{
    "progress":  200
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete project (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Project not found: does-not-exist"}]}
```

## /document-categories
### Create document category (happy)
Request:
```json
{
    "color":  "#0055FF",
    "key":  "contract-",
    "name":  "Contract"
}
```
Response: status=
```json
Object reference not set to an instance of an object.
```

### Create document category (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"key"},{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be blank","message":"must not be blank","field":"color"}]}
```

### List document categories
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get document category (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get document category (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update document category (happy)
Request:
```json
{
    "color":  "#000000"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete document category (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Document category not found: does-not-exist"}]}
```

## /project-types
### Create project type (happy)
Request:
```json
{
    "key":  "fixed-",
    "name":  "Fixed Price",
    "description":  "Fixed scope"
}
```
Response: status=
```json
Object reference not set to an instance of an object.
```

### Create project type (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be blank","message":"must not be blank","field":"key"},{"code":"must not be blank","message":"must not be blank","field":"name"}]}
```

### List project types
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get project type (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get project type (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update project type (happy)
Request:
```json
{
    "description":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete project type (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Project type not found: does-not-exist"}]}
```

## /contract-types
### Create contract type (happy)
Request:
```json
{
    "key":  "enterprise-",
    "name":  "Enterprise",
    "description":  "Enterprise agreement"
}
```
Response: status=
```json
Object reference not set to an instance of an object.
```

### Create contract type (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be blank","message":"must not be blank","field":"key"},{"code":"must not be blank","message":"must not be blank","field":"description"}]}
```

### List contract types
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get contract type (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get contract type (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update contract type (happy)
Request:
```json
{
    "description":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete contract type (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Contract type not found: does-not-exist"}]}
```

## /opportunities
### Create opportunity (happy)
Request:
```json
{
    "stage":  "proposal",
    "probability":  45,
    "clientName":  null,
    "title":  "New rollout",
    "expectedCloseDate":  "2026-03-01",
    "expectedValue":  90000,
    "clientId":  null,
    "notes":  "Awaiting review"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be blank","message":"must not be blank","field":"clientName"}]}
```

### Create opportunity (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"title"},{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be null","message":"must not be null","field":"expectedCloseDate"},{"code":"must not be blank","message":"must not be blank","field":"notes"},{"code":"must not be null","message":"must not be null","field":"expectedValue"},{"code":"must not be null","message":"must not be null","field":"stage"},{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be null","message":"must not be null","field":"probability"}]}
```

### Create opportunity (invalid enum)
Request:
```json
{
    "stage":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List opportunities
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get opportunity (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get opportunity (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update opportunity (happy)
Request:
```json
{
    "notes":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete opportunity (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Opportunity not found: does-not-exist"}]}
```

## /communication-logs
### Create communication log (happy)
Request:
```json
{
    "summary":  "Weekly sync",
    "clientId":  null,
    "type":  "meeting",
    "participants":  [
                         "Alice",
                         "Bob"
                     ],
    "clientName":  null,
    "projectId":  null,
    "projectName":  null,
    "actionItems":  [
                        "Send summary"
                    ],
    "date":  "2026-02-05"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be blank","message":"must not be blank","field":"clientName"}]}
```

### Create communication log (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be null","message":"must not be null","field":"actionItems"},{"code":"must not be null","message":"must not be null","field":"date"},{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be blank","message":"must not be blank","field":"summary"},{"code":"must not be null","message":"must not be null","field":"participants"},{"code":"must not be null","message":"must not be null","field":"type"}]}
```

### Create communication log (invalid enum)
Request:
```json
{
    "type":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List communication logs
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get communication log (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get communication log (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update communication log (happy)
Request:
```json
{
    "summary":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete communication log (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Communication log not found: does-not-exist"}]}
```

## /epics
### Create epic (happy)
Request:
```json
{
    "projectName":  null,
    "title":  "Platform Migration",
    "progress":  15,
    "projectId":  null,
    "description":  "Migrate core services",
    "status":  "in-progress",
    "priority":  "must",
    "storyCount":  4
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create epic (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be null","message":"must not be null","field":"priority"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be null","message":"must not be null","field":"progress"},{"code":"must not be blank","message":"must not be blank","field":"title"},{"code":"must not be null","message":"must not be null","field":"storyCount"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create epic (invalid enum)
Request:
```json
{
    "status":  "bad",
    "priority":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List epics
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get epic (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get epic (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update epic (happy)
Request:
```json
{
    "progress":  30
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete epic (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Epic not found: does-not-exist"}]}
```

## /user-stories
### Create user story (happy)
Request:
```json
{
    "effort":  8,
    "description":  "Port auth service",
    "projectName":  null,
    "projectId":  null,
    "notes":  "Needs review",
    "acceptanceCriteria":  [
                               "All tests pass"
                           ],
    "epicTitle":  "Platform Migration",
    "status":  "backlog",
    "epicId":  null,
    "title":  "Migrate auth module",
    "priority":  "must"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"epicId"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create user story (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"epicTitle"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"title"},{"code":"must not be blank","message":"must not be blank","field":"notes"},{"code":"must not be null","message":"must not be null","field":"effort"},{"code":"must not be blank","message":"must not be blank","field":"epicId"},{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be null","message":"must not be null","field":"acceptanceCriteria"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be null","message":"must not be null","field":"priority"}]}
```

### Create user story (invalid enum)
Request:
```json
{
    "status":  "bad",
    "priority":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List user stories
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get user story (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get user story (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update user story (happy)
Request:
```json
{
    "notes":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete user story (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"User story not found: does-not-exist"}]}
```

## /tasks
### Create task (happy)
Request:
```json
{
    "actualHours":  0,
    "description":  "Draft migration plan",
    "estimatedHours":  16,
    "projectName":  null,
    "projectId":  null,
    "owner":  "Alice",
    "ownerType":  "internal",
    "status":  "todo",
    "title":  "Create migration plan",
    "priority":  "must",
    "dueDate":  "2026-02-08"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create task (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be blank","message":"must not be blank","field":"owner"},{"code":"must not be null","message":"must not be null","field":"actualHours"},{"code":"must not be null","message":"must not be null","field":"priority"},{"code":"must not be null","message":"must not be null","field":"ownerType"},{"code":"must not be null","message":"must not be null","field":"dueDate"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be null","message":"must not be null","field":"estimatedHours"},{"code":"must not be blank","message":"must not be blank","field":"title"},{"code":"must not be null","message":"must not be null","field":"status"}]}
```

### Create task (invalid enum)
Request:
```json
{
    "priority":  "bad",
    "ownerType":  "bad",
    "status":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List tasks
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get task (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get task (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update task (happy)
Request:
```json
{
    "status":  "in-progress"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete task (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Task not found: does-not-exist"}]}
```

## /issues
### Create issue (happy)
Request:
```json
{
    "description":  "Dependency needs upgrade",
    "mitigationPlan":  "Upgrade",
    "projectName":  null,
    "projectId":  null,
    "owner":  "Bob",
    "status":  "open",
    "severity":  "medium",
    "title":  "Legacy dependency",
    "dueDate":  "2026-02-10"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"projectName"}]}
```

### Create issue (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be null","message":"must not be null","field":"severity"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be blank","message":"must not be blank","field":"owner"},{"code":"must not be null","message":"must not be null","field":"dueDate"},{"code":"must not be blank","message":"must not be blank","field":"mitigationPlan"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be blank","message":"must not be blank","field":"title"}]}
```

### Create issue (invalid enum)
Request:
```json
{
    "status":  "bad",
    "severity":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List issues
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get issue (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get issue (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update issue (happy)
Request:
```json
{
    "status":  "resolved"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete issue (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Issue not found: does-not-exist"}]}
```

## /risks
### Create risk (happy)
Request:
```json
{
    "description":  "Scope may increase",
    "mitigationPlan":  "Control",
    "projectName":  null,
    "projectId":  null,
    "probability":  30,
    "owner":  "Claire",
    "status":  "identified",
    "impact":  60,
    "title":  "Scope creep",
    "dueDate":  "2026-02-15"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create risk (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"description"},{"code":"must not be null","message":"must not be null","field":"probability"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be null","message":"must not be null","field":"dueDate"},{"code":"must not be blank","message":"must not be blank","field":"owner"},{"code":"must not be blank","message":"must not be blank","field":"mitigationPlan"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"title"},{"code":"must not be null","message":"must not be null","field":"impact"}]}
```

### Create risk (invalid enum)
Request:
```json
{
    "status":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List risks
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get risk (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get risk (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update risk (happy)
Request:
```json
{
    "status":  "mitigated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete risk (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Risk not found: does-not-exist"}]}
```

## /milestones
### Create milestone (happy)
Request:
```json
{
    "projectName":  null,
    "deliverable":  "Discovery report",
    "dueDate":  "2026-02-12",
    "signOffBy":  "Lead PM",
    "projectId":  null,
    "name":  "Discovery Complete",
    "status":  "pending",
    "acceptanceCriteria":  "Sign-off"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"projectId"}]}
```

### Create milestone (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"acceptanceCriteria"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"signOffBy"},{"code":"must not be null","message":"must not be null","field":"dueDate"},{"code":"must not be null","message":"must not be null","field":"status"},{"code":"must not be blank","message":"must not be blank","field":"deliverable"}]}
```

### Create milestone (invalid enum)
Request:
```json
{
    "status":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List milestones
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get milestone (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get milestone (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update milestone (happy)
Request:
```json
{
    "status":  "completed"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete milestone (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Milestone not found: does-not-exist"}]}
```

## /schedules
### Create schedule (happy)
Request:
```json
{
    "scheduleEndDate":  "2026-02-07",
    "clientName":  null,
    "clientContactPhone":  "+33123456789",
    "clientContactName":  "Jean Dupont",
    "createdBy":  "Scheduler",
    "projectId":  null,
    "reminderEnabled":  true,
    "scheduleColor":  "#FFAA00",
    "clientContactEmail":  "test20260205091803285@example.com",
    "scheduleNotes":  "Initial",
    "clientId":  null,
    "projectName":  null,
    "isScheduled":  true,
    "scheduleStartDate":  "2026-02-05"
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be null","message":"must not be null","field":"reminderEnabled"},{"code":"must not be null","message":"must not be null","field":"isScheduled"},{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be null","message":"must not be null","field":"scheduleStartDate"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"scheduleColor"},{"code":"must not be null","message":"must not be null","field":"scheduleEndDate"}]}
```

### Create schedule (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be null","message":"must not be null","field":"reminderEnabled"},{"code":"must not be null","message":"must not be null","field":"isScheduled"},{"code":"must not be blank","message":"must not be blank","field":"clientId"},{"code":"must not be null","message":"must not be null","field":"scheduleStartDate"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"scheduleColor"},{"code":"must not be null","message":"must not be null","field":"scheduleEndDate"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"clientName"}]}
```

### List schedules
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get schedule (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get schedule (not found)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update schedule (happy)
Request:
```json
{
    "scheduleColor":  "#00AAFF",
    "scheduleNotes":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Delete schedule (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Schedule not found: does-not-exist"}]}
```

## /reports
### Reports summary
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Reports revenue
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Reports pipeline
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

## /dashboard
### Dashboard overview
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Dashboard stats
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

## /contacts
### Create contact (happy)
Request:
```json
{
    "name":  "Nadia",
    "service":  "Consulting",
    "company":  "Nadia Co",
    "email":  "nadia@example.com",
    "budget":  "5000",
    "message":  "Need estimate"
}
```
Response: status=
```json
Object reference not set to an instance of an object.
```

### Create contact (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"name"},{"code":"must not be blank","message":"must not be blank","field":"message"},{"code":"must not be blank","message":"must not be blank","field":"email"}]}
```

### List contacts
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get contact (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update contact (happy)
Request:
```json
{
    "isRead":  true,
    "status":  "in-progress"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Create contact reply (happy)
Request:
```json
{
    "message":  "Thanks"
}
```
Response: status=403
```json

```

### Create contact reply (missing required)
Request:
```json
{

}
```
Response: status=403
```json

```

### Delete contact (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Contact not found: does-not-exist"}]}
```

## /invoices
### Create invoice (happy)
Request:
```json
{
    "clientAddress":  "10 Rue de Paris",
    "invoiceNumber":  "INV-2026-001",
    "clientName":  null,
    "dueDate":  "2026-02-20",
    "projectId":  null,
    "type":  "client",
    "amount":  6400,
    "status":  "sent",
    "currency":  "EUR",
    "clientId":  null,
    "projectName":  null,
    "notes":  "Net 14 days",
    "date":  "2026-02-04",
    "items":  [
                  {
                      "unitPrice":  800,
                      "description":  "Consulting days",
                      "total":  4000,
                      "quantity":  5
                  },
                  {
                      "unitPrice":  1200,
                      "description":  "Workshops",
                      "total":  2400,
                      "quantity":  2
                  }
              ]
}
```
Response: status=400
```json
{"errors":[{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"clientName"}]}
```

### Create invoice (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be null","message":"must not be null","field":"date"},{"code":"must not be blank","message":"must not be blank","field":"notes"},{"code":"must not be blank","message":"must not be blank","field":"invoiceNumber"},{"code":"must not be blank","message":"must not be blank","field":"projectId"},{"code":"must not be blank","message":"must not be blank","field":"type"},{"code":"must not be blank","message":"must not be blank","field":"projectName"},{"code":"must not be blank","message":"must not be blank","field":"clientName"},{"code":"must not be blank","message":"must not be blank","field":"currency"},{"code":"must not be null","message":"must not be null","field":"dueDate"},{"code":"must not be null","message":"must not be null","field":"status"}]}
```

### Create invoice (invalid enum)
Request:
```json
{
    "status":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List invoices
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get invoice (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get invoice (not found)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Invoice not found: does-not-exist"}]}
```

### Update invoice (happy)
Request:
```json
{
    "notes":  "Updated"
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Update invoice status (happy)
Request:
```json
{
    "status":  "paid"
}
```
Response: status=403
```json

```

### Update invoice status (invalid enum)
Request:
```json
{
    "status":  "bad"
}
```
Response: status=403
```json

```

### Delete invoice (invalid id)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Invoice not found: does-not-exist"}]}
```

## /documents
### Create document metadata (happy)
Request:
```json
{
    "clientId":  null,
    "size":  "12KB",
    "clientName":  null,
    "category":  "contract",
    "projectName":  null,
    "uploadedAt":  "2026-02-05",
    "uploadedBy":  "System",
    "name":  "contract.pdf",
    "fileType":  "PDF",
    "projectId":  null
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Invalid request: entityId or projectId/clientId is required"}]}
```

### Create document metadata (missing required)
Request:
```json
{

}
```
Response: status=400
```json
{"errors":[{"code":"must not be null","message":"must not be null","field":"category"},{"code":"must not be blank","message":"must not be blank","field":"uploadedBy"}]}
```

### Create document metadata (invalid enum)
Request:
```json
{
    "category":  "bad"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Malformed JSON request"}]}
```

### List documents
Response: status=
```json
Object reference not set to an instance of an object.
```

### Get document (happy)
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Get document (not found)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Document not found: does-not-exist"}]}
```

### Download document (not found)
Response: status=404
```json
{"errors":[{"code":"ENTITY_NOT_FOUND","message":"Document not found: does-not-exist"}]}
```

### Upload document (happy)
Request:
```json
{
    "clientId":  null,
    "size":  "1600 KB",
    "clientName":  null,
    "category":  "contract",
    "projectName":  null,
    "uploadedAt":  "2026-02-05",
    "uploadedBy":  "System",
    "name":  "1600w-a1RYzvS1EFo.webp",
    "fileType":  "WEBP",
    "projectId":  null
}
```
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Download uploaded document
Response: status=403
```json

```

### Delete uploaded document
Response: status=500
```json
{"errors":[{"code":"INTERNAL_EXCEPTION","message":"An internal exception occurred, please try again or contact the admin"}]}
```

### Upload document (missing file)
Request:
```json
{
    "category":  "contract",
    "uploadedBy":  "System"
}
```
Response: status=400
```json
{"errors":[{"code":"INVALID_REQUEST","message":"Invalid request: entityId or projectId/clientId is required"}]}
```

## /openapi
### OpenAPI docs
Response: status=
```json
Object reference not set to an instance of an object.
```



## Summary (Detailed)

- /clients
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /projects
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /document-categories
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /project-types
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /contract-types
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /opportunities
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /communication-logs
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /epics
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /user-stories
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /tasks
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /issues
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /risks
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /milestones
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /schedules
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /reports
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /dashboard
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /contacts
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /invoices
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /documents
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 

- /openapi
  Total cases: 0
  Success: 0
  Error: Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant. Exception calling "Join" with "2" argument(s): "Value cannot be null.
Parameter name: values" Cannot overwrite variable Error because it is read-only or constant.
  Status codes: 



## Summary (Detailed)

- /clients
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /projects
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /document-categories
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /project-types
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /contract-types
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /opportunities
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /communication-logs
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /epics
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /user-stories
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /tasks
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /issues
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /risks
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /milestones
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /schedules
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /reports
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /dashboard
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /contacts
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /invoices
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /documents
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 

- /openapi
  Total cases: 0
  Success: 0
  Error: 0
  Status codes: 


