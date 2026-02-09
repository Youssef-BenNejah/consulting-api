# GENERIC_PROJECT_DOCUMENTATION_TEMPLATE.md

> Use this template to generate a **complete, mega-detailed** documentation package for any backend project. It mirrors the depth and structure of the Tictak docs.
> Replace all placeholders in `{{LIKE_THIS}}` and remove sections that do not apply. Keep formatting consistent so tools can parse it.

---

## 0) Project Metadata

- **Project Name:** `{{PROJECT_NAME}}`
- **Repository Location:** `{{ABSOLUTE_REPO_PATH}}`
- **Primary Language/Runtime:** `{{LANGUAGE_VERSION}}`
- **Framework(s):** `{{FRAMEWORKS}}`
- **Architecture Style:** `{{MONOLITH/MICROSERVICES/EVENT_DRIVEN/etc}}`
- **Deployment Targets:** `{{ENVIRONMENTS}}`
- **Database(s):** `{{DATABASES}}`
- **External Services:** `{{EXTERNAL_SERVICES}}`

---

## 1) Overview

**One-paragraph summary:**

{{SUMMARY_PARAGRAPH}}

**What this system handles (bullets):**
- `{{CAPABILITY_1}}`
- `{{CAPABILITY_2}}`
- `{{CAPABILITY_3}}`
- `{{CAPABILITY_4}}`

---

## 2) Repository Structure (High-Level)

**Top-level structure:**
- `{{TOP_LEVEL_DIR_1}}` - `{{PURPOSE}}`
- `{{TOP_LEVEL_DIR_2}}` - `{{PURPOSE}}`
- `{{TOP_LEVEL_DIR_3}}` - `{{PURPOSE}}`

**Key internal packages/modules:**
- `{{MODULE_PATH_1}}` - `{{PURPOSE}}`
- `{{MODULE_PATH_2}}` - `{{PURPOSE}}`
- `{{MODULE_PATH_3}}` - `{{PURPOSE}}`

---

## 3) Runtime Dependencies & External Integrations

**Core dependencies/libraries:**
- `{{LIB_1}}`
- `{{LIB_2}}`
- `{{LIB_3}}`

**External services:**
- `{{SERVICE_1}}`
- `{{SERVICE_2}}`
- `{{SERVICE_3}}`

---

## 4) Configuration & Profiles

**Config files and profiles:**
- `{{CONFIG_FILE_1}}`
- `{{CONFIG_FILE_2}}`
- `{{CONFIG_FILE_3}}`

**Notable settings (list key env vars, properties, flags):**
- `{{SETTING_1}}`
- `{{SETTING_2}}`
- `{{SETTING_3}}`

---

## 5) Security Model

### 5.1 Authentication

- **Auth scheme:** `{{JWT/OAUTH/SESSION/etc}}`
- **Token location:** `{{HEADER/COOKIE/etc}}`
- **Token claims:** `{{CLAIM_LIST}}`
- **Password policy:** `{{PASSWORD_RULES}}`

### 5.2 Authorization

- **Role system:** `{{ROLES}}`
- **Enforcement location:** `{{ANNOTATIONS/MIDDLEWARE/GUARDS}}`
- **Known gaps:** `{{GAPS_OR_NONE}}`

### 5.3 Security Middleware / Filters

**Key files:**
- `{{SECURITY_FILE_1}}`
- `{{SECURITY_FILE_2}}`

---

## 6) Real-Time / Messaging (If Applicable)

**Transport:** `{{WEBSOCKET/STOMP/SSE/MQ/etc}}`

**Endpoints/Channels:**
- `{{CHANNEL_1}}` - `{{DESCRIPTION}}`
- `{{CHANNEL_2}}` - `{{DESCRIPTION}}`

**Auth/Handshake:**
- `{{HANDSHAKE_DETAILS}}`

**Known issues:**
- `{{ISSUE_1_OR_NONE}}`

---

## 7) Core Business Flows (Super Detailed)

> For each core flow, provide step-by-step logic, state transitions, timeouts, concurrency, and notifications.

### 7.1 Flow: `{{FLOW_NAME_1}}`

**Primary class/service:** `{{CLASS_OR_MODULE}}`

**State tracking:**
- `{{STATE_KEY_1}}`
- `{{STATE_KEY_2}}`

**Step-by-step:**
1. `{{STEP_1}}`
2. `{{STEP_2}}`
3. `{{STEP_3}}`

**Edge cases:**
- `{{EDGE_1}}`
- `{{EDGE_2}}`

**Known issues:**
- `{{ISSUE_1_OR_NONE}}`

### 7.2 Flow: `{{FLOW_NAME_2}}`

(Repeat same structure for each major flow.)

---

## 8) Data Model (Database Documents/Tables)

### 8.1 Entity Relationship Summary

- `{{ENTITY_A}}` -> `{{ENTITY_B}}` (`{{RELATION}}`)
- `{{ENTITY_C}}` -> `{{ENTITY_D}}` (`{{RELATION}}`)

### 8.2 Full Model Attributes (Auto-Extracted)

> For each model/entity, list every field and type. If the model is empty, show an empty entry.

**Model: `{{MODEL_NAME}}`**
Source: `{{MODEL_FILE_PATH}}`
- `{{FIELD_NAME_1}}`: `{{TYPE}}`
- `{{FIELD_NAME_2}}`: `{{TYPE}}`

(Repeat for every model.)

---

## 9) DTOs / API Contracts (Requests & Responses)

> For each request/response DTO, list all fields and types (including validation annotations if present).

**DTO: `{{DTO_NAME}}`**
Source: `{{DTO_FILE_PATH}}`
- `{{FIELD_NAME_1}}`: `{{TYPE_OR_VALIDATION}}`
- `{{FIELD_NAME_2}}`: `{{TYPE_OR_VALIDATION}}`

(Repeat for every DTO.)

---

## 10) API Reference (All Endpoints)

> Each endpoint must include method, URL, auth intent, request, response, description, status codes, validation rules, and edge cases.

### 10.1 `{{CONTROLLER_OR_GROUP}}`

1) `{{METHOD}} {{PATH}}`
- **Auth:** `{{ROLE_OR_PUBLIC}}`
- **Request:** `{{DTO_OR_SHAPE}}`
- **Response:** `{{DTO_OR_SHAPE}}`
- **Status Codes:** `{{STATUS_LIST}}`
- **Validation:** `{{VALIDATION_RULES}}`
- **Description:** `{{DESCRIPTION}}`
- **Edge Cases:** `{{EDGE_CASES}}`

(Repeat for every endpoint.)

---

## 11) Email / Templates / Files

**Templates & usage:**
- `{{TEMPLATE_FILE_1}}` - `{{PURPOSE}}`
- `{{TEMPLATE_FILE_2}}` - `{{PURPOSE}}`

**Generation logic:**
- `{{SERVICE_OR_CLASS}}` - `{{DETAILS}}`

---

## 12) Scheduled Jobs / Cron / Background Workers

- `{{JOB_NAME_1}}` - `{{SCHEDULE}}` - `{{PURPOSE}}`
- `{{JOB_NAME_2}}` - `{{SCHEDULE}}` - `{{PURPOSE}}`

---

## 13) Feature Catalog (Grouped)

### 13.1 By Flow
- `{{FLOW_GROUP_1}}`
- `{{FLOW_GROUP_2}}`

### 13.2 By Theme
- `{{THEME_GROUP_1}}`
- `{{THEME_GROUP_2}}`

### 13.3 By Lifecycle
- `{{LIFECYCLE_GROUP_1}}`
- `{{LIFECYCLE_GROUP_2}}`

---

## 14) Test Cases (Comprehensive)

### 14.1 Auth & Security
- `{{TEST_CASE_1}}`
- `{{TEST_CASE_2}}`

### 14.2 Core Flow Tests
- `{{TEST_CASE_3}}`
- `{{TEST_CASE_4}}`

### 14.3 External Integrations
- `{{TEST_CASE_5}}`
- `{{TEST_CASE_6}}`

### 14.4 Reliability & Failure Modes
- `{{TEST_CASE_7}}`
- `{{TEST_CASE_8}}`

---

## 15) Secrets & Credentials (Redacted)

**Do NOT include real secrets. Only list the locations and types.**

**Locations with secrets:**
- `{{SECRET_FILE_1}}` - `{{SECRET_TYPE}}`
- `{{SECRET_FILE_2}}` - `{{SECRET_TYPE}}`

**Required actions:**
- `{{ACTION_1}}`
- `{{ACTION_2}}`

---

## 16) Appendices & Indexes

- `{{MODEL_INDEX_FILE}}`
- `{{DTO_INDEX_FILE}}`
- `{{ENDPOINT_INDEX_FILE}}`

---

## 17) Scrum Breakdown (Epics -> Features -> Stories -> Tickets)

> Use this to recreate the mega Scrum doc with hyper-detailed tickets.

### Epic `{{EPIC_NAME}}`

**Feature:** `{{FEATURE_NAME}}`

**Story:** `{{STORY_NAME}}`

**Ticket:** `{{TICKET_ID}} {{TICKET_TITLE}}`
- **Endpoint:** `{{METHOD}} {{PATH}}`
- **Request DTO:** `{{DTO}}`
- **Success Response:** `{{STATUS}} + {{RESPONSE_BODY}}`
- **Errors:** `{{ERROR_CODES}}`
- **Validation:** `{{VALIDATION_RULES}}`
- **Data Touched:** `{{DB_COLLECTIONS/TABLES}}`
- **Business Rules:** `{{RULES}}`
- **Acceptance Criteria:** `{{CRITERIA}}`
- **Edge Cases:** `{{EDGE_CASES}}`
- **Dependencies:** `{{DEPENDENCIES}}`
- **Tests:** `{{TESTS}}`

(Repeat for every ticket.)

---

## 18) Full Endpoint Catalog Table (Optional)

| Controller | Method | Path | Signature |
|---|---|---|---|
| {{CONTROLLER}} | {{METHOD}} | {{PATH}} | {{SIGNATURE}} |

---

## 19) Notes & Known Issues

- `{{NOTE_1}}`
- `{{NOTE_2}}`

---

## 20) How This Doc Was Generated

- **Source of truth:** `{{SOURCE_OF_TRUTH}}`
- **Extraction method:** `{{SCRIPTS_OR_TOOLS}}`
- **Last updated:** `{{DATE}}`

---

**End of documentation template.**
