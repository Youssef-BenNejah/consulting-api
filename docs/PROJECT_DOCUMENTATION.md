# PROJECT_DOCUMENTATION.md

> Generated from the generic templates in docs/ with project-specific data filled in.

---

## 0) Project Metadata

- **Project Name:** `consultant-business-api`
- **Repository Location:** `C:\Users\arebh\Documents\dashboard cherif\back`
- **Primary Language/Runtime:** `Java 17`
- **Framework(s):** `Spring Boot 3.5.4 (parent), Spring Web, Spring Security, Spring Data MongoDB`
- **Architecture Style:** `Monolith REST API`
- **Deployment Targets:** `Local dev (mvnw), Docker Compose (maildev); other environments not specified`
- **Database(s):** `MongoDB`
- **External Services:** `SMTP (MailDev for local), Cloudinary`

---

## 1) Overview

**One-paragraph summary:**

Consultant Business API is a Spring Boot backend for managing consulting operations, including clients, projects, invoices, documents, schedules, settings, and reporting. It exposes REST endpoints under `/api/v1` with JWT-based authentication and integrates with MongoDB, SMTP mail, and optional Cloudinary storage.

**What this system handles (bullets):**
- `User authentication and password recovery`
- `Core business entities: clients, projects, invoices, documents, schedules, contacts, communications`
- `Settings catalogs and notifications`
- `Dashboard and reporting summaries`

---

## 2) Repository Structure (High-Level)

**Top-level structure:**
- `src/` - `Application source code`
- `docs/` - `Documentation and scripts`
- `tests/` - `Test data (if any)`

**Key internal packages/modules:**
- `src/main/java/brama/consultant_business_api/controller` - `REST controllers`
- `src/main/java/brama/consultant_business_api/service` - `Business logic services`
- `src/main/java/brama/consultant_business_api/domain` - `Domain models, DTOs, mappers`
- `src/main/java/brama/consultant_business_api/security` - `JWT auth, filters, security config`

---

## 3) Runtime Dependencies & External Integrations

**Core dependencies/libraries:**
- `org.springframework.boot:spring-boot-starter-data-mongodb`
- `org.springframework.boot:spring-boot-starter-security`
- `org.springframework.boot:spring-boot-starter-validation`
- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-mail`
- `org.springframework.boot:spring-boot-starter-thymeleaf`
- `io.jsonwebtoken:jjwt-api@0.12.6`
- `io.jsonwebtoken:jjwt-jackson@0.12.6`
- `io.jsonwebtoken:jjwt-impl@0.12.6`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui@2.7.0`
- `com.cloudinary:cloudinary-http44@1.38.0`
- `org.projectlombok:lombok`
- `org.springframework.boot:spring-boot-starter-test`
- `org.springframework.security:spring-security-test`

**External services:**
- `MongoDB Atlas/cluster (via DB_CLUSTER_URL)`
- `SMTP server (MailDev for local development)`
- `Cloudinary (optional, for storage)`

---

## 4) Configuration & Profiles

**Config files and profiles:**
- `src/main/resources/application.yml`
- `.env`
- `.env.example`
- `docker-compose.yml`

**Notable settings (list key env vars, properties, flags):**
- `CLOUDINARY_API_KEY`
- `CLOUDINARY_API_SECRET`
- `CLOUDINARY_CLOUD_NAME`
- `CLOUDINARY_ENABLED`
- `CLOUDINARY_FOLDER`
- `CORS_ALLOWED_ORIGINS`
- `DB_CLUSTER_URL`
- `DB_NAME`
- `DB_PASSWORD`
- `DB_USERNAME`
- `MAIL_FROM`
- `SMTP_AUTH`
- `SMTP_HOST`
- `SMTP_PASSWORD`
- `SMTP_PORT`
- `SMTP_STARTTLS`
- `SMTP_USERNAME`
- `app.security.jwt.access-token-expiration`
- `app.security.jwt.refresh-token-expiration`
- `app.security.otp.expiration-minutes`
- `app.cors.allowed-origins`

---

## 5) Security Model

### 5.1 Authentication

- **Auth scheme:** `JWT (asymmetric keys loaded from resources/keys/local-only)`
- **Token location:** `Authorization: Bearer <token>`
- **Token claims:** `token_type, user_id, roles, sub (email)`
- **Password policy:** `Not explicitly enforced in code beyond validation annotations`

### 5.2 Authorization

- **Role system:** `Role documents and role IDs on User; authorities built as ROLE_<ROLE_NAME>`
- **Enforcement location:** `Method security enabled; no @PreAuthorize detected in code`
- **Known gaps:** `SecurityConfig permits /api/v1/** publicly; endpoints appear effectively public`

### 5.3 Security Middleware / Filters

**Key files:**
- `src/main/java/brama/consultant_business_api/security/SecurityConfig.java`
- `src/main/java/brama/consultant_business_api/security/JwtFilter.java`
- `src/main/java/brama/consultant_business_api/security/JwtService.java`

---

## 6) Real-Time / Messaging (If Applicable)

**Transport:** `Not implemented`

**Endpoints/Channels:**
- `N/A`

**Auth/Handshake:**
- `N/A`

**Known issues:**
- `None`

---

## 7) Core Business Flows (Super Detailed)

### 7.1 Flow: `Authentication (Login + Refresh)`

**Primary class/service:** `AuthenticationServiceImpl`

**State tracking:**
- `User.enabled, User.locked, User.credentialsExpired`
- `JWT claims: token_type, user_id, roles`

**Step-by-step:**
1. `Client submits AuthenticationRequest (email/password).`
2. `AuthenticationManager validates credentials and loads UserDetails.`
3. `JwtService generates access token (roles + user_id) and refresh token.`
4. `AuthenticationResponse returns tokens.`
5. `Refresh endpoint validates refresh token and issues a new access token.`

**Edge cases:**
- `Invalid credentials -> auth failure`
- `Refresh token expired or wrong token_type -> error`

**Known issues:**
- `Endpoints appear public unless explicitly protected elsewhere`

### 7.2 Flow: `Password Reset via OTP`

**Primary class/service:** `ForgotPasswordServiceImpl`

**State tracking:**
- `PasswordResetOtp` document with `otpHash`, `createdAt`, `expiresAt`, `used`

**Step-by-step:**
1. `User requests password reset with email.`
2. `OTP is generated, hashed, stored in MongoDB, and emailed.`
3. `User submits OTP for verification.`
4. `OTP is validated (hash match, not expired, not used).`
5. `User submits new password and OTP is marked used.`

**Edge cases:**
- `OTP expired or already used`
- `Email not found`

**Known issues:**
- `None observed`

### 7.3 Flow: `Project + Invoice Lifecycle (High-Level)`

**Primary class/service:** `ProjectService, InvoiceService`

**State tracking:**
- `Project status, health, schedule`
- `Invoice status and items`

**Step-by-step:**
1. `Create client and project (ClientCreateRequest, ProjectCreateRequest).`
2. `Assign project type, status, priority, and schedule.`
3. `Generate invoices with items and status changes.`
4. `Reports aggregate margins and financial summaries.`

**Edge cases:**
- `Invalid references to client/project types`
- `Invoice export with missing data`

**Known issues:**
- `None observed`

---

## 8) Data Model (Database Documents/Tables)

### 8.1 Entity Relationship Summary

- `Invoice` -> `InvoiceItem` (`List`)
- `SettingsCatalog` -> `SettingsProjectStatus` (`List`)
- `SettingsCatalog` -> `SettingsPriority` (`List`)
- `SettingsCatalog` -> `SettingsTag` (`List`)

### 8.2 Full Model Attributes (Auto-Extracted)

**Model: `PasswordResetOtp`**
Source: `src/main/java/brama/consultant_business_api/auth/otp/PasswordResetOtp.java`
- `id`: `String`
- `email`: `String`
- `otpHash`: `String`
- `createdAt`: `Instant`
- `expiresAt`: `Instant`
- `used`: `boolean`

**Model: `Client`**
Source: `src/main/java/brama/consultant_business_api/domain/client/model/Client.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `industry`: `String`
- `country`: `String`
- `primaryContact`: `String`
- `email`: `String`
- `phone`: `String`
- `contractType`: `String`
- `notes`: `String`
- `tags`: `List<String>`
- `createdAt`: `LocalDate`

**Model: `CommunicationLog`**
Source: `src/main/java/brama/consultant_business_api/domain/communication/model/CommunicationLog.java`
- `extends`: `BaseDocument`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `date`: `LocalDate`
- `type`: `CommunicationType`
- `summary`: `String`
- `actionItems`: `List<String>`
- `participants`: `List<String>`

**Model: `Contact`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/model/Contact.java`
- `id`: `String`
- `name`: `String`
- `email`: `String`
- `company`: `String`
- `service`: `String`
- `budget`: `String`
- `message`: `String`
- `status`: `ContactStatus`
- `isRead`: `boolean`
- `createdAt`: `Instant`
- `updatedAt`: `Instant`

**Model: `ContactReply`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/model/ContactReply.java`
- `id`: `String`
- `contactId`: `String`
- `message`: `String`
- `sentAt`: `Instant`

**Model: `ContractTypeConfig`**
Source: `src/main/java/brama/consultant_business_api/domain/contracttype/model/ContractTypeConfig.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**Model: `DocumentFile`**
Source: `src/main/java/brama/consultant_business_api/domain/document/model/DocumentFile.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `category`: `DocumentCategoryKey`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `entityType`: `DocumentEntityType`
- `entityId`: `String`
- `uploadedBy`: `String`
- `uploadedAt`: `LocalDate`
- `size`: `String`
- `fileType`: `String`
- `fileId`: `String`
- `fileUrl`: `String`
- `storageProvider`: `String`
- `resourceType`: `String`

**Model: `DocumentCategory`**
Source: `src/main/java/brama/consultant_business_api/domain/documentcategory/model/DocumentCategory.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `key`: `String`
- `color`: `String`

**Model: `Invoice`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/model/Invoice.java`
- `extends`: `BaseDocument`
- `invoiceNumber`: `String`
- `type`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `clientAddress`: `String`
- `date`: `LocalDate`
- `dueDate`: `LocalDate`
- `amount`: `Double`
- `currency`: `String`
- `status`: `InvoiceStatus`
- `notes`: `String`
- `items`: `List<InvoiceItem>`

**Model: `InvoiceItem`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/model/InvoiceItem.java`
- `id`: `String`
- `description`: `String`
- `quantity`: `Double`
- `unitPrice`: `Double`
- `total`: `Double`

**Model: `Project`**
Source: `src/main/java/brama/consultant_business_api/domain/project/model/Project.java`
- `extends`: `BaseDocument`
- `projectId`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `name`: `String`
- `description`: `String`
- `startDate`: `LocalDate`
- `endDate`: `LocalDate`
- `statusId`: `String`
- `projectTypeId`: `String`
- `priorityId`: `String`
- `tagIds`: `List<String>`
- `contractTypeId`: `String`
- `clientBudget`: `Double`
- `vendorCost`: `Double`
- `internalCost`: `Double`
- `healthStatus`: `HealthStatus`
- `progress`: `Integer`

**Model: `ProjectTypeConfig`**
Source: `src/main/java/brama/consultant_business_api/domain/projecttype/model/ProjectTypeConfig.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**Model: `ProjectSchedule`**
Source: `src/main/java/brama/consultant_business_api/domain/schedule/model/ProjectSchedule.java`
- `id`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `clientContactName`: `String`
- `clientContactEmail`: `String`
- `clientContactPhone`: `String`
- `scheduleStartDate`: `LocalDate`
- `scheduleEndDate`: `LocalDate`
- `scheduleDurationDays`: `Integer`
- `scheduleColor`: `String`
- `scheduleNotes`: `String`
- `isScheduled`: `boolean`
- `reminderEnabled`: `boolean`
- `createdBy`: `String`
- `createdAt`: `Instant`
- `updatedAt`: `Instant`

**Model: `SettingsCatalog`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/catalog/model/SettingsCatalog.java`
- `extends`: `BaseDocument`
- `projectStatuses`: `List<SettingsProjectStatus>`
- `priorities`: `List<SettingsPriority>`
- `tags`: `List<SettingsTag>`

**Model: `SettingsPriority`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/catalog/model/SettingsPriority.java`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`

**Model: `SettingsProjectStatus`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/catalog/model/SettingsProjectStatus.java`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`
- `category`: `String`

**Model: `SettingsTag`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/catalog/model/SettingsTag.java`
- `id`: `String`
- `name`: `String`
- `color`: `String`

**Model: `GeneralSettings`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/general/model/GeneralSettings.java`
- `extends`: `BaseDocument`
- `companyName`: `String`
- `email`: `String`
- `timezone`: `String`
- `defaultCurrency`: `String`
- `darkMode`: `Boolean`
- `compactMode`: `Boolean`

**Model: `NotificationSettings`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/notification/model/NotificationSettings.java`
- `extends`: `BaseDocument`
- `emailNotifications`: `Boolean`
- `slackIntegration`: `Boolean`
- `milestoneReminders`: `Boolean`
- `invoiceDueAlerts`: `Boolean`
- `weeklyDigest`: `Boolean`

**Model: `Role`**
Source: `src/main/java/brama/consultant_business_api/role/Role.java`
- `extends`: `BaseDocument`
- `name`: `String`
- `description`: `String`
- `permissions`: `List<String>`

**Model: `User`**
Source: `src/main/java/brama/consultant_business_api/user/User.java`
- `id`: `String`
- `firstName`: `String`
- `lastName`: `String`
- `email`: `String`
- `phoneNumber`: `String`
- `password`: `String`
- `dateOfBirth`: `LocalDate`
- `enabled`: `boolean`
- `locked`: `boolean`
- `credentialsExpired`: `boolean`
- `emailVerified`: `boolean`
- `phoneVerified`: `boolean`
- `createdDate`: `LocalDateTime`
- `lastModifiedDate`: `LocalDateTime`
- `roles`: `List<String>`

---

## 9) DTOs / API Contracts (Requests & Responses)

**DTO: `AuthenticationRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/AuthenticationRequest.java`
- `category`: `in`
- `No fields detected`

**DTO: `ForgotPasswordRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/ForgotPasswordRequest.java`
- `category`: `in`
- `email`: `@NotBlank(message = "VALIDATION.FORGOT_PASSWORD.EMAIL.NOT_BLANK") @Email(message = "VALIDATION.FORGOT_PASSWORD.EMAIL.FORMAT") @Schema(example = "user@example.com") String`

**DTO: `RefreshRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/RefreshRequest.java`
- `category`: `in`
- `refreshToken`: `String`

**DTO: `RegistrationRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/RegistrationRequest.java`
- `category`: `in`
- `No fields detected`

**DTO: `ResetPasswordRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/ResetPasswordRequest.java`
- `category`: `in`
- `email`: `@NotBlank(message = "VALIDATION.RESET_PASSWORD.EMAIL.NOT_BLANK") @Email(message = "VALIDATION.RESET_PASSWORD.EMAIL.FORMAT") @Schema(example = "user@example.com") String`
- `otp`: `@NotBlank(message = "VALIDATION.RESET_PASSWORD.OTP.NOT_BLANK") @Pattern(regexp = "^\\d{6}$", message = "VALIDATION.RESET_PASSWORD.OTP.FORMAT") @Schema(example = "123456") String`
- `newPassword`: `@NotBlank(message = "VALIDATION.RESET_PASSWORD.PASSWORD.NOT_BLANK") @Size(min = 8, max = 72, message = "VALIDATION.RESET_PASSWORD.PASSWORD.SIZE") @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$", @Schema(example = "pAssword1!_") String`
- `confirmPassword`: `@NotBlank(message = "VALIDATION.RESET_PASSWORD.CONFIRM_PASSWORD.NOT_BLANK") @Size(min = 8, max = 72, message = "VALIDATION.RESET_PASSWORD.CONFIRM_PASSWORD.SIZE") @Schema(example = "pAssword1!_") String`

**DTO: `VerifyOtpRequest`**
Source: `src/main/java/brama/consultant_business_api/auth/request/VerifyOtpRequest.java`
- `category`: `in`
- `email`: `@NotBlank(message = "VALIDATION.VERIFY_OTP.EMAIL.NOT_BLANK") @Email(message = "VALIDATION.VERIFY_OTP.EMAIL.FORMAT") @Schema(example = "user@example.com") String`
- `otp`: `@NotBlank(message = "VALIDATION.VERIFY_OTP.OTP.NOT_BLANK") @Pattern(regexp = "^\\d{6}$", message = "VALIDATION.VERIFY_OTP.OTP.FORMAT") @Schema(example = "123456") String`

**DTO: `AuthenticationResponse`**
Source: `src/main/java/brama/consultant_business_api/auth/response/AuthenticationResponse.java`
- `category`: `out`
- `accessToken`: `@JsonProperty("access_token") String`
- `refreshToken`: `@JsonProperty("refresh_token") String`
- `tokenType`: `@JsonProperty("token_type") String`

**DTO: `ForgotPasswordResponse`**
Source: `src/main/java/brama/consultant_business_api/auth/response/ForgotPasswordResponse.java`
- `category`: `out`
- `message`: `String`
- `email`: `String`

**DTO: `OtpResponse`**
Source: `src/main/java/brama/consultant_business_api/auth/response/OtpResponse.java`
- `category`: `out`
- `message`: `String`
- `verified`: `boolean`

**DTO: `ResetPasswordResponse`**
Source: `src/main/java/brama/consultant_business_api/auth/response/ResetPasswordResponse.java`
- `category`: `out`
- `message`: `String`
- `success`: `boolean`

**DTO: `ClientCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/client/dto/request/ClientCreateRequest.java`
- `category`: `in`
- `name`: `@NotBlank String`
- `industry`: `@NotBlank String`
- `country`: `@NotBlank String`
- `primaryContact`: `@NotBlank String`
- `email`: `@NotBlank @Email String`
- `phone`: `@NotBlank String`
- `contractType`: `@NotBlank String`
- `notes`: `@NotBlank String`
- `tags`: `@NotNull List<String>`

**DTO: `ClientUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/client/dto/request/ClientUpdateRequest.java`
- `category`: `in`
- `name`: `String`
- `industry`: `String`
- `country`: `String`
- `primaryContact`: `String`
- `email`: `@Email String`
- `phone`: `String`
- `contractType`: `String`
- `notes`: `String`
- `tags`: `List<String>`

**DTO: `ClientResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/client/dto/response/ClientResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `industry`: `String`
- `country`: `String`
- `primaryContact`: `String`
- `email`: `String`
- `phone`: `String`
- `contractType`: `String`
- `notes`: `String`
- `tags`: `List<String>`
- `createdAt`: `LocalDate`
- `updatedAt`: `LocalDateTime`

**DTO: `CommunicationLogCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/communication/dto/request/CommunicationLogCreateRequest.java`
- `category`: `in`
- `clientId`: `@NotBlank String`
- `clientName`: `@NotBlank String`
- `projectId`: `String`
- `projectName`: `String`
- `date`: `@NotNull LocalDate`
- `type`: `@NotNull CommunicationType`
- `summary`: `@NotBlank String`
- `actionItems`: `@NotNull List<String>`
- `participants`: `@NotNull List<String>`

**DTO: `CommunicationLogUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/communication/dto/request/CommunicationLogUpdateRequest.java`
- `category`: `in`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `date`: `LocalDate`
- `type`: `CommunicationType`
- `summary`: `String`
- `actionItems`: `List<String>`
- `participants`: `List<String>`

**DTO: `CommunicationLogResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/communication/dto/response/CommunicationLogResponse.java`
- `category`: `out`
- `id`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `date`: `LocalDate`
- `type`: `CommunicationType`
- `summary`: `String`
- `actionItems`: `List<String>`
- `participants`: `List<String>`

**DTO: `ContactCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/dto/request/ContactCreateRequest.java`
- `category`: `in`
- `name`: `@NotBlank String`
- `email`: `@NotBlank @Email String`
- `company`: `String`
- `service`: `String`
- `budget`: `String`
- `message`: `@NotBlank String`

**DTO: `ContactReplyCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/dto/request/ContactReplyCreateRequest.java`
- `category`: `in`
- `message`: `@NotBlank String`

**DTO: `ContactUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/dto/request/ContactUpdateRequest.java`
- `category`: `in`
- `status`: `ContactStatus`
- `isRead`: `@JsonProperty("is_read") @JsonAlias("isRead") Boolean`

**DTO: `ContactReplyResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/dto/response/ContactReplyResponse.java`
- `category`: `out`
- `id`: `String`
- `contactId`: `@JsonProperty("contact_id") String`
- `message`: `String`
- `sentAt`: `@JsonProperty("sent_at") Instant`

**DTO: `ContactResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/contact/dto/response/ContactResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `email`: `String`
- `company`: `String`
- `service`: `String`
- `budget`: `String`
- `message`: `String`
- `status`: `ContactStatus`
- `isRead`: `@JsonProperty("is_read") boolean`
- `createdAt`: `@JsonProperty("created_at") Instant`
- `updatedAt`: `@JsonProperty("updated_at") Instant`

**DTO: `ContractTypeCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/contracttype/dto/request/ContractTypeCreateRequest.java`
- `category`: `in`
- `name`: `@NotBlank String`
- `key`: `@NotBlank String`
- `description`: `@NotBlank String`

**DTO: `ContractTypeUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/contracttype/dto/request/ContractTypeUpdateRequest.java`
- `category`: `in`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `ContractTypeResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/contracttype/dto/response/ContractTypeResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `DashboardNotification`**
Source: `src/main/java/brama/consultant_business_api/domain/dashboard/dto/response/DashboardNotification.java`
- `category`: `out`
- `id`: `String`
- `type`: `String`
- `title`: `String`
- `description`: `String`
- `link`: `String`

**DTO: `DashboardSummaryResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/dashboard/dto/response/DashboardSummaryResponse.java`
- `category`: `out`
- `totalClients`: `long`
- `activeProjects`: `long`
- `projectsByHealth`: `ProjectsByHealthResponse`
- `financials`: `FinancialsResponse`

**DTO: `FinancialsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/dashboard/dto/response/FinancialsResponse.java`
- `category`: `out`
- `totalBilled`: `double`
- `totalReceived`: `double`
- `outstandingReceivables`: `double`

**DTO: `ProjectsByHealthResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/dashboard/dto/response/ProjectsByHealthResponse.java`
- `category`: `out`
- `green`: `long`
- `amber`: `long`
- `red`: `long`

**DTO: `DocumentCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/document/dto/request/DocumentCreateRequest.java`
- `category`: `in`
- `name`: `String`
- `category`: `@NotNull DocumentCategoryKey`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `entityType`: `DocumentEntityType`
- `entityId`: `String`
- `uploadedBy`: `@NotBlank String`
- `uploadedAt`: `LocalDate`
- `size`: `String`
- `fileType`: `String`

**DTO: `DocumentDownload`**
Source: `src/main/java/brama/consultant_business_api/domain/document/dto/response/DocumentDownload.java`
- `category`: `out`
- `content`: `byte[]`
- `filename`: `String`
- `contentType`: `String`

**DTO: `DocumentResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/document/dto/response/DocumentResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `category`: `DocumentCategoryKey`
- `clientId`: `String`
- `clientName`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `entityType`: `DocumentEntityType`
- `entityId`: `String`
- `uploadedBy`: `String`
- `uploadedAt`: `LocalDate`
- `size`: `String`
- `fileType`: `String`
- `fileId`: `String`
- `fileUrl`: `String`
- `storageProvider`: `String`
- `resourceType`: `String`

**DTO: `DocumentCategoryCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/documentcategory/dto/request/DocumentCategoryCreateRequest.java`
- `category`: `in`
- `name`: `@NotBlank String`
- `key`: `@NotBlank String`
- `color`: `@NotBlank String`

**DTO: `DocumentCategoryUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/documentcategory/dto/request/DocumentCategoryUpdateRequest.java`
- `category`: `in`
- `name`: `String`
- `key`: `String`
- `color`: `String`

**DTO: `DocumentCategoryResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/documentcategory/dto/response/DocumentCategoryResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `color`: `String`

**DTO: `InvoiceCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/request/InvoiceCreateRequest.java`
- `category`: `in`
- `invoiceNumber`: `@NotBlank String`
- `type`: `@NotBlank String`
- `projectId`: `@NotBlank String`
- `projectName`: `@NotBlank String`
- `clientId`: `String`
- `clientName`: `@NotBlank String`
- `clientAddress`: `String`
- `date`: `@NotNull LocalDate`
- `dueDate`: `@NotNull LocalDate`
- `amount`: `@PositiveOrZero Double`
- `currency`: `@NotBlank String`
- `status`: `@NotNull InvoiceStatus`
- `notes`: `@NotBlank String`
- `items`: `@Valid List<InvoiceItemRequest>`

**DTO: `InvoiceExportRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/request/InvoiceExportRequest.java`
- `category`: `in`
- `ids`: `List<String>`

**DTO: `InvoiceItemRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/request/InvoiceItemRequest.java`
- `category`: `in`
- `id`: `String`
- `description`: `@NotBlank String`
- `quantity`: `@NotNull @PositiveOrZero Double`
- `unitPrice`: `@NotNull @PositiveOrZero Double`
- `total`: `@PositiveOrZero Double`

**DTO: `InvoiceStatusUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/request/InvoiceStatusUpdateRequest.java`
- `category`: `in`
- `status`: `@NotNull InvoiceStatus`

**DTO: `InvoiceUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/request/InvoiceUpdateRequest.java`
- `category`: `in`
- `invoiceNumber`: `String`
- `type`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `clientAddress`: `String`
- `date`: `LocalDate`
- `dueDate`: `LocalDate`
- `amount`: `@PositiveOrZero Double`
- `currency`: `String`
- `status`: `InvoiceStatus`
- `notes`: `String`
- `items`: `@Valid List<InvoiceItemRequest>`

**DTO: `InvoiceItemResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/response/InvoiceItemResponse.java`
- `category`: `out`
- `id`: `String`
- `description`: `String`
- `quantity`: `Double`
- `unitPrice`: `Double`
- `total`: `Double`

**DTO: `InvoiceResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/invoice/dto/response/InvoiceResponse.java`
- `category`: `out`
- `id`: `String`
- `invoiceNumber`: `String`
- `type`: `String`
- `projectId`: `String`
- `projectName`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `clientAddress`: `String`
- `date`: `LocalDate`
- `dueDate`: `LocalDate`
- `amount`: `Double`
- `currency`: `String`
- `status`: `InvoiceStatus`
- `notes`: `String`
- `items`: `List<InvoiceItemResponse>`
- `dueLabel`: `String`
- `dueColor`: `String`

**DTO: `ProjectCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/project/dto/request/ProjectCreateRequest.java`
- `category`: `in`
- `projectId`: `@NotBlank String`
- `clientId`: `@NotBlank String`
- `clientName`: `@NotBlank String`
- `name`: `@NotBlank String`
- `description`: `@NotBlank String`
- `startDate`: `@NotNull LocalDate`
- `endDate`: `@NotNull LocalDate`
- `statusId`: `@NotBlank String`
- `projectTypeId`: `@NotBlank String`
- `priorityId`: `String`
- `tagIds`: `List<String>`
- `contractTypeId`: `String`
- `clientBudget`: `@NotNull @PositiveOrZero Double`
- `vendorCost`: `@NotNull @PositiveOrZero Double`
- `internalCost`: `@NotNull @PositiveOrZero Double`
- `healthStatus`: `@NotNull HealthStatus`
- `progress`: `@NotNull @Min(0) @Max(100) Integer`

**DTO: `ProjectUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/project/dto/request/ProjectUpdateRequest.java`
- `category`: `in`
- `projectId`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `name`: `String`
- `description`: `String`
- `startDate`: `LocalDate`
- `endDate`: `LocalDate`
- `statusId`: `String`
- `projectTypeId`: `String`
- `priorityId`: `String`
- `tagIds`: `List<String>`
- `contractTypeId`: `String`
- `clientBudget`: `@PositiveOrZero Double`
- `vendorCost`: `@PositiveOrZero Double`
- `internalCost`: `@PositiveOrZero Double`
- `healthStatus`: `HealthStatus`
- `progress`: `@Min(0) @Max(100) Integer`

**DTO: `ProjectResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/project/dto/response/ProjectResponse.java`
- `category`: `out`
- `id`: `String`
- `projectId`: `String`
- `clientId`: `String`
- `clientName`: `String`
- `name`: `String`
- `description`: `String`
- `startDate`: `LocalDate`
- `endDate`: `LocalDate`
- `statusId`: `String`
- `projectTypeId`: `String`
- `priorityId`: `String`
- `tagIds`: `List<String>`
- `contractTypeId`: `String`
- `status`: `ProjectStatusSettingsResponse`
- `projectType`: `ProjectTypeResponse`
- `priority`: `PrioritySettingsResponse`
- `tags`: `List<TagSettingsResponse>`
- `contractType`: `ContractTypeResponse`
- `clientBudget`: `Double`
- `vendorCost`: `Double`
- `internalCost`: `Double`
- `healthStatus`: `HealthStatus`
- `progress`: `Integer`
- `createdAt`: `LocalDateTime`
- `updatedAt`: `LocalDateTime`

**DTO: `ProjectTypeCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/projecttype/dto/request/ProjectTypeCreateRequest.java`
- `category`: `in`
- `name`: `@NotBlank String`
- `key`: `@NotBlank String`
- `description`: `@NotBlank String`

**DTO: `ProjectTypeUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/projecttype/dto/request/ProjectTypeUpdateRequest.java`
- `category`: `in`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `ProjectTypeResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/projecttype/dto/response/ProjectTypeResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `InvoicesReportResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/InvoicesReportResponse.java`
- `category`: `out`
- `totalReceivables`: `double`
- `overdueInvoices`: `long`
- `totalInvoices`: `long`
- `overdue`: `List<InvoiceResponse>`

**DTO: `MarginsReportResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/MarginsReportResponse.java`
- `category`: `out`
- `totalRevenue`: `double`
- `totalCost`: `double`
- `totalMargin`: `double`
- `avgMarginPercent`: `double`
- `projects`: `List<ProjectMarginItemResponse>`

**DTO: `ProjectMarginItemResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/ProjectMarginItemResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `clientName`: `String`
- `clientBudget`: `double`
- `vendorCost`: `double`
- `internalCost`: `double`
- `margin`: `double`
- `marginPercent`: `double`

**DTO: `ProjectsReportResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/ProjectsReportResponse.java`
- `category`: `out`
- `statusCounts`: `ProjectStatusCountsResponse`
- `projects`: `List<ProjectResponse>`

**DTO: `ProjectStatusCountsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/ProjectStatusCountsResponse.java`
- `category`: `out`
- `delivery`: `long`
- `discovery`: `long`
- `review`: `long`
- `closed`: `long`
- `other`: `long`

**DTO: `ReportsOverviewResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/report/dto/response/ReportsOverviewResponse.java`
- `category`: `out`
- `totalRevenue`: `double`
- `totalCost`: `double`
- `totalMargin`: `double`
- `avgMarginPercent`: `double`
- `totalReceivables`: `double`
- `overdueInvoices`: `long`
- `totalInvoices`: `long`

**DTO: `ScheduleCreateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/schedule/dto/request/ScheduleCreateRequest.java`
- `category`: `in`
- `projectId`: `@NotBlank @JsonProperty("project_id") @JsonAlias("projectId") String`
- `projectName`: `@NotBlank @JsonProperty("project_name") @JsonAlias("projectName") String`
- `clientId`: `@NotBlank @JsonProperty("client_id") @JsonAlias("clientId") String`
- `clientName`: `@NotBlank @JsonProperty("client_name") @JsonAlias("clientName") String`
- `clientContactName`: `@JsonProperty("client_contact_name") @JsonAlias("clientContactName") String`
- `clientContactEmail`: `@JsonProperty("client_contact_email") @JsonAlias("clientContactEmail") String`
- `clientContactPhone`: `@JsonProperty("client_contact_phone") @JsonAlias("clientContactPhone") String`
- `scheduleStartDate`: `@NotNull @JsonProperty("schedule_start_date") @JsonAlias("scheduleStartDate") LocalDate`
- `scheduleEndDate`: `@NotNull @JsonProperty("schedule_end_date") @JsonAlias("scheduleEndDate") LocalDate`
- `scheduleColor`: `@NotBlank @JsonProperty("schedule_color") @JsonAlias("scheduleColor") String`
- `scheduleNotes`: `@JsonProperty("schedule_notes") @JsonAlias("scheduleNotes") String`
- `isScheduled`: `@NotNull @JsonProperty("is_scheduled") @JsonAlias("isScheduled") Boolean`
- `reminderEnabled`: `@NotNull @JsonProperty("reminder_enabled") @JsonAlias("reminderEnabled") Boolean`
- `createdBy`: `@JsonProperty("created_by") @JsonAlias("createdBy") String`

**DTO: `ScheduleUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/schedule/dto/request/ScheduleUpdateRequest.java`
- `category`: `in`
- `projectId`: `@JsonProperty("project_id") @JsonAlias("projectId") String`
- `projectName`: `@JsonProperty("project_name") @JsonAlias("projectName") String`
- `clientId`: `@JsonProperty("client_id") @JsonAlias("clientId") String`
- `clientName`: `@JsonProperty("client_name") @JsonAlias("clientName") String`
- `clientContactName`: `@JsonProperty("client_contact_name") @JsonAlias("clientContactName") String`
- `clientContactEmail`: `@JsonProperty("client_contact_email") @JsonAlias("clientContactEmail") String`
- `clientContactPhone`: `@JsonProperty("client_contact_phone") @JsonAlias("clientContactPhone") String`
- `scheduleStartDate`: `@JsonProperty("schedule_start_date") @JsonAlias("scheduleStartDate") LocalDate`
- `scheduleEndDate`: `@JsonProperty("schedule_end_date") @JsonAlias("scheduleEndDate") LocalDate`
- `scheduleColor`: `@JsonProperty("schedule_color") @JsonAlias("scheduleColor") String`
- `scheduleNotes`: `@JsonProperty("schedule_notes") @JsonAlias("scheduleNotes") String`
- `isScheduled`: `@JsonProperty("is_scheduled") @JsonAlias("isScheduled") Boolean`
- `reminderEnabled`: `@JsonProperty("reminder_enabled") @JsonAlias("reminderEnabled") Boolean`
- `createdBy`: `@JsonProperty("created_by") @JsonAlias("createdBy") String`

**DTO: `ScheduleResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/schedule/dto/response/ScheduleResponse.java`
- `category`: `out`
- `id`: `String`
- `projectId`: `@JsonProperty("project_id") String`
- `projectName`: `@JsonProperty("project_name") String`
- `clientId`: `@JsonProperty("client_id") String`
- `clientName`: `@JsonProperty("client_name") String`
- `clientContactName`: `@JsonProperty("client_contact_name") String`
- `clientContactEmail`: `@JsonProperty("client_contact_email") String`
- `clientContactPhone`: `@JsonProperty("client_contact_phone") String`
- `scheduleStartDate`: `@JsonProperty("schedule_start_date") LocalDate`
- `scheduleEndDate`: `@JsonProperty("schedule_end_date") LocalDate`
- `scheduleDurationDays`: `@JsonProperty("schedule_duration_days") Integer`
- `scheduleColor`: `@JsonProperty("schedule_color") String`
- `scheduleNotes`: `@JsonProperty("schedule_notes") String`
- `isScheduled`: `@JsonProperty("is_scheduled") boolean`
- `reminderEnabled`: `@JsonProperty("reminder_enabled") boolean`
- `createdBy`: `@JsonProperty("created_by") String`
- `createdAt`: `@JsonProperty("created_at") Instant`
- `updatedAt`: `@JsonProperty("updated_at") Instant`

**DTO: `ContractTypeUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/ContractTypeUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `DocumentCategoryUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/DocumentCategoryUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `color`: `String`

**DTO: `PriorityUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/PriorityUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`

**DTO: `ProjectStatusUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/ProjectStatusUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`
- `category`: `String`

**DTO: `ProjectTypeUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/ProjectTypeUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `name`: `String`
- `key`: `String`
- `description`: `String`

**DTO: `RoleUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/RoleUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `name`: `String`
- `description`: `String`
- `permissions`: `List<String>`

**DTO: `TagUpsertRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/items/TagUpsertRequest.java`
- `category`: `in`
- `id`: `String`
- `name`: `String`
- `color`: `String`

**DTO: `SettingsPatchRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/request/SettingsPatchRequest.java`
- `category`: `in`
- `replaceAll`: `Boolean`
- `documentCategories`: `@Valid List<DocumentCategoryUpsertRequest>`
- `documentCategoryDeletes`: `List<String>`
- `replaceDocumentCategories`: `Boolean`
- `projectTypes`: `@Valid List<ProjectTypeUpsertRequest>`
- `projectTypeDeletes`: `List<String>`
- `replaceProjectTypes`: `Boolean`
- `contractTypes`: `@Valid List<ContractTypeUpsertRequest>`
- `contractTypeDeletes`: `List<String>`
- `replaceContractTypes`: `Boolean`
- `projectStatuses`: `@Valid List<ProjectStatusUpsertRequest>`
- `projectStatusDeletes`: `List<String>`
- `replaceProjectStatuses`: `Boolean`
- `priorities`: `@Valid List<PriorityUpsertRequest>`
- `priorityDeletes`: `List<String>`
- `replacePriorities`: `Boolean`
- `tags`: `@Valid List<TagUpsertRequest>`
- `tagDeletes`: `List<String>`
- `replaceTags`: `Boolean`
- `roles`: `@Valid List<RoleUpsertRequest>`
- `roleDeletes`: `List<String>`
- `replaceRoles`: `Boolean`
- `general`: `@Valid GeneralSettingsPatchRequest`
- `notifications`: `@Valid NotificationSettingsPatchRequest`

**DTO: `PrioritySettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/response/items/PrioritySettingsResponse.java`
- `category`: `out`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`

**DTO: `ProjectStatusSettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/response/items/ProjectStatusSettingsResponse.java`
- `category`: `out`
- `id`: `String`
- `key`: `String`
- `name`: `String`
- `color`: `String`
- `category`: `String`

**DTO: `RoleSettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/response/items/RoleSettingsResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `description`: `String`
- `permissions`: `List<String>`

**DTO: `TagSettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/response/items/TagSettingsResponse.java`
- `category`: `out`
- `id`: `String`
- `name`: `String`
- `color`: `String`

**DTO: `SettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/dto/response/SettingsResponse.java`
- `category`: `out`
- `documentCategories`: `List<DocumentCategoryResponse>`
- `projectTypes`: `List<ProjectTypeResponse>`
- `contractTypes`: `List<ContractTypeResponse>`
- `projectStatuses`: `List<ProjectStatusSettingsResponse>`
- `priorities`: `List<PrioritySettingsResponse>`
- `tags`: `List<TagSettingsResponse>`
- `roles`: `List<RoleSettingsResponse>`
- `general`: `GeneralSettingsResponse`
- `notifications`: `NotificationSettingsResponse`

**DTO: `GeneralSettingsPatchRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/general/dto/request/GeneralSettingsPatchRequest.java`
- `category`: `in`
- `companyName`: `String`
- `email`: `String`
- `timezone`: `String`
- `defaultCurrency`: `String`
- `darkMode`: `Boolean`
- `compactMode`: `Boolean`

**DTO: `GeneralSettingsUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/general/dto/request/GeneralSettingsUpdateRequest.java`
- `category`: `in`
- `companyName`: `String`
- `email`: `String`
- `timezone`: `String`
- `defaultCurrency`: `String`
- `darkMode`: `Boolean`
- `compactMode`: `Boolean`

**DTO: `GeneralSettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/general/dto/response/GeneralSettingsResponse.java`
- `category`: `out`
- `companyName`: `String`
- `email`: `String`
- `timezone`: `String`
- `defaultCurrency`: `String`
- `darkMode`: `Boolean`
- `compactMode`: `Boolean`

**DTO: `NotificationSettingsPatchRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/notification/dto/request/NotificationSettingsPatchRequest.java`
- `category`: `in`
- `emailNotifications`: `Boolean`
- `slackIntegration`: `Boolean`
- `milestoneReminders`: `Boolean`
- `invoiceDueAlerts`: `Boolean`
- `weeklyDigest`: `Boolean`

**DTO: `NotificationSettingsUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/notification/dto/request/NotificationSettingsUpdateRequest.java`
- `category`: `in`
- `emailNotifications`: `Boolean`
- `slackIntegration`: `Boolean`
- `milestoneReminders`: `Boolean`
- `invoiceDueAlerts`: `Boolean`
- `weeklyDigest`: `Boolean`

**DTO: `NotificationSettingsResponse`**
Source: `src/main/java/brama/consultant_business_api/domain/settings/notification/dto/response/NotificationSettingsResponse.java`
- `category`: `out`
- `emailNotifications`: `Boolean`
- `slackIntegration`: `Boolean`
- `milestoneReminders`: `Boolean`
- `invoiceDueAlerts`: `Boolean`
- `weeklyDigest`: `Boolean`

**DTO: `ChangePasswordRequest`**
Source: `src/main/java/brama/consultant_business_api/user/request/ChangePasswordRequest.java`
- `category`: `in`
- `currentPassword`: `String`
- `newPassword`: `String`
- `confirmNewPassword`: `String`

**DTO: `ProfileUpdateRequest`**
Source: `src/main/java/brama/consultant_business_api/user/request/ProfileUpdateRequest.java`
- `category`: `in`
- `firstName`: `String`
- `lastName`: `String`
- `dateOfBirth`: `LocalDate`

---

## 10) API Reference (All Endpoints)

### 10.1 `AuthenticationController`

1) `POST /api/v1/auth/login`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `AuthenticationRequest`
- **Response:** `ResponseEntity<AuthenticationResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/auth/register`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `RegistrationRequest`
- **Response:** `ResponseEntity<Void>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `POST /api/v1/auth/refresh`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `RefreshRequest`
- **Response:** `ResponseEntity<AuthenticationResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ClientController`

1) `GET /api/v1/clients`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ClientResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/clients`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ClientCreateRequest`
- **Response:** `ApiResponse<ClientResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/clients/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ClientResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/clients/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ClientUpdateRequest`
- **Response:** `ApiResponse<ClientResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/clients/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `GET /api/v1/clients/{clientId}/projects`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ProjectResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `CommunicationLogController`

1) `GET /api/v1/communication-logs`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<CommunicationLogResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/communication-logs`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `CommunicationLogCreateRequest`
- **Response:** `ApiResponse<CommunicationLogResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/communication-logs/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<CommunicationLogResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/communication-logs/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `CommunicationLogUpdateRequest`
- **Response:** `ApiResponse<CommunicationLogResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/communication-logs/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ContactController`

1) `POST /api/v1/contacts`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ContactCreateRequest`
- **Response:** `ApiResponse<ContactResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `GET /api/v1/contacts`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ContactResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/contacts/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ContactResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/contacts/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ContactUpdateRequest`
- **Response:** `ApiResponse<ContactResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/contacts/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `GET /api/v1/contacts/{id}/replies`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ContactReplyResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

7) `POST /api/v1/contacts/{id}/replies`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ContactReplyCreateRequest`
- **Response:** `ApiResponse<ContactReplyResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ContractTypeController`

1) `GET /api/v1/contract-types`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ContractTypeResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/contract-types`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ContractTypeCreateRequest`
- **Response:** `ApiResponse<ContractTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/contract-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ContractTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/contract-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ContractTypeUpdateRequest`
- **Response:** `ApiResponse<ContractTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/contract-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `DashboardController`

1) `GET /api/v1/dashboard/summary`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<DashboardSummaryResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `GET /api/v1/dashboard/notifications`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<DashboardNotification>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `DocumentCategoryController`

1) `GET /api/v1/document-categories`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<DocumentCategoryResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/document-categories`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `DocumentCategoryCreateRequest`
- **Response:** `ApiResponse<DocumentCategoryResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/document-categories/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<DocumentCategoryResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/document-categories/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `DocumentCategoryUpdateRequest`
- **Response:** `ApiResponse<DocumentCategoryResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/document-categories/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `DocumentController`

1) `GET /api/v1/documents`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<DocumentResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/documents`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `DocumentCreateRequest`
- **Response:** `ApiResponse<DocumentResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `POST /api/v1/documents`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<DocumentResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `GET /api/v1/documents/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<DocumentResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/documents/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `GET /api/v1/documents/{id}/download`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `N/A`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ForgotPasswordController`

1) `POST /api/auth/forgot-password`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ForgotPasswordRequest`
- **Response:** `ResponseEntity<ForgotPasswordResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/auth/verify-otp`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `VerifyOtpRequest`
- **Response:** `ResponseEntity<OtpResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `POST /api/auth/reset-password`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ResetPasswordRequest`
- **Response:** `ResponseEntity<ResetPasswordResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `InvoiceController`

1) `GET /api/v1/invoices`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<InvoiceResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/invoices`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `InvoiceCreateRequest`
- **Response:** `ApiResponse<InvoiceResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/invoices/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<InvoiceResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/invoices/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `InvoiceUpdateRequest`
- **Response:** `ApiResponse<InvoiceResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/invoices/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `POST /api/v1/invoices/{id}/status`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `InvoiceStatusUpdateRequest`
- **Response:** `ApiResponse<InvoiceResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

7) `POST /api/v1/invoices/export`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `InvoiceExportRequest`
- **Response:** `N/A`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ProjectController`

1) `GET /api/v1/projects`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ProjectResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/projects`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ProjectCreateRequest`
- **Response:** `ApiResponse<ProjectResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/projects/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ProjectResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/projects/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ProjectUpdateRequest`
- **Response:** `ApiResponse<ProjectResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/projects/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `GET /api/v1/projects/{projectId}/client`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ClientResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

7) `GET /api/v1/projects/{projectId}/documents`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<DocumentResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

8) `GET /api/v1/projects/{projectId}/planning`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ScheduleResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

9) `GET /api/v1/projects/{projectId}/invoices`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<InvoiceResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

10) `GET /api/v1/projects/{projectId}/finances`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<FinancialsResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ProjectTypeController`

1) `GET /api/v1/project-types`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ProjectTypeResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/project-types`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ProjectTypeCreateRequest`
- **Response:** `ApiResponse<ProjectTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/project-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ProjectTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/project-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ProjectTypeUpdateRequest`
- **Response:** `ApiResponse<ProjectTypeResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/project-types/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ReportsController`

1) `GET /api/v1/reports/overview`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ReportsOverviewResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `GET /api/v1/reports/projects`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ProjectsReportResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/reports/margins`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<MarginsReportResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `GET /api/v1/reports/invoices`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<InvoicesReportResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `ScheduleController`

1) `GET /api/v1/schedules`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<List<ScheduleResponse>>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/schedules`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ScheduleCreateRequest`
- **Response:** `ApiResponse<ScheduleResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `GET /api/v1/schedules/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<ScheduleResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/schedules/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `ScheduleUpdateRequest`
- **Response:** `ApiResponse<ScheduleResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/schedules/{id}`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

6) `GET /api/v1/schedules/export`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `N/A`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `SettingsController`

1) `GET /api/v1/settings`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `ApiResponse<SettingsResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `PATCH /api/v1/settings`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `SettingsPatchRequest`
- **Response:** `ApiResponse<SettingsResponse>`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

### 10.1 `UserController`

1) `PATCH /api/v1/users/me`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

2) `POST /api/v1/users/me/password`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

3) `PATCH /api/v1/users/me/deactivate`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

4) `PATCH /api/v1/users/me/reactivate`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

5) `DELETE /api/v1/users/me`
- **Auth:** `public (no method-level restrictions detected)`
- **Request:** `N/A`
- **Response:** `void`
- **Status Codes:** `N/A (not explicitly defined in code)`
- **Validation:** `See DTO annotations`
- **Description:** `N/A`
- **Edge Cases:** `N/A`

---

## 11) Email / Templates / Files

**Templates & usage:**
- `src/main/resources/templates/otp-email.html` - `OTP email template for password reset`

**Generation logic:**
- `src/main/java/brama/consultant_business_api/mail` - `Email service and template rendering`

---

## 12) Scheduled Jobs / Cron / Background Workers

- `None detected (@Scheduled not found)`

---

## 13) Feature Catalog (Grouped)

### 13.1 By Flow
- `Authentication & Password Reset`
- `Client and Project Management`
- `Invoices and Reporting`
- `Documents and Categories`
- `Schedules and Settings`

### 13.2 By Theme
- `Security`
- `Operational Data`
- `Analytics/Reporting`
- `Configuration`

### 13.3 By Lifecycle
- `Create/Update/Delete entities`
- `Generate reports`
- `Recover account access`

---

## 14) Test Cases (Comprehensive)

### 14.1 Auth & Security
- `AuthenticationService tests (if present)`
- `Security filter and JWT validation tests (not detected)`

### 14.2 Core Flow Tests
- `Mapper tests for contact/invoice/schedule`
- `Service tests per domain module (client, project, invoice, etc.)`

### 14.3 External Integrations
- `Mail and Cloudinary integration tests (not detected)`

### 14.4 Reliability & Failure Modes
- `Error handling tests (not detected)`

---

## 15) Secrets & Credentials (Redacted)

**Do NOT include real secrets. Only list the locations and types.**

**Locations with secrets:**
- `.env` - `Database, SMTP, Cloudinary credentials`
- `src/main/resources/keys/local-only/private_key.pem` - `JWT signing private key`
- `src/main/resources/keys/local-only/public_key.pem` - `JWT verification public key`

**Required actions:**
- `Ensure .env is configured for DB and SMTP before running`
- `Protect JWT key files in non-local environments`

---

## 16) Appendices & Indexes

- `N/A`

---

## 17) Scrum Breakdown (Epics -> Features -> Stories -> Tickets)

> Not defined in repo. Recommend extracting from product management artifacts if needed.

---

## 18) Full Endpoint Catalog Table (Optional)

| Controller | Method | Path | Signature |
|---|---|---|---|
| AuthenticationController | POST | /api/v1/auth/login | public ResponseEntity<AuthenticationResponse> login( |
| AuthenticationController | POST | /api/v1/auth/register | public ResponseEntity<Void> register( |
| AuthenticationController | POST | /api/v1/auth/refresh | public ResponseEntity<AuthenticationResponse> refresh( |
| ForgotPasswordController | POST | /api/auth/forgot-password | public ResponseEntity<ForgotPasswordResponse> forgotPassword( |
| ForgotPasswordController | POST | /api/auth/verify-otp | public ResponseEntity<OtpResponse> verifyOtp( |
| ForgotPasswordController | POST | /api/auth/reset-password | public ResponseEntity<ResetPasswordResponse> resetPassword( |
| ClientController | GET | /api/v1/clients | public ApiResponse<List<ClientResponse>> listClients( |
| ClientController | POST | /api/v1/clients | public ApiResponse<ClientResponse> createClient(@Valid @RequestBody final ClientCreateRequest request) { |
| ClientController | GET | /api/v1/clients/{id} | public ApiResponse<ClientResponse> getClient(@PathVariable final String id) { |
| ClientController | PATCH | /api/v1/clients/{id} | public ApiResponse<ClientResponse> updateClient(@PathVariable final String id, |
| ClientController | DELETE | /api/v1/clients/{id} | public void deleteClient(@PathVariable final String id) { |
| ClientController | GET | /api/v1/clients/{clientId}/projects | public ApiResponse<List<ProjectResponse>> getProjectsByClientId(@PathVariable final String clientId, |
| CommunicationLogController | GET | /api/v1/communication-logs | public ApiResponse<List<CommunicationLogResponse>> listLogs( |
| CommunicationLogController | POST | /api/v1/communication-logs | public ApiResponse<CommunicationLogResponse> createLog(@Valid @RequestBody final CommunicationLogCreateRequest request) { |
| CommunicationLogController | GET | /api/v1/communication-logs/{id} | public ApiResponse<CommunicationLogResponse> getLog(@PathVariable final String id) { |
| CommunicationLogController | PATCH | /api/v1/communication-logs/{id} | public ApiResponse<CommunicationLogResponse> updateLog(@PathVariable final String id, |
| CommunicationLogController | DELETE | /api/v1/communication-logs/{id} | public void deleteLog(@PathVariable final String id) { |
| ContactController | POST | /api/v1/contacts | public ApiResponse<ContactResponse> createContact(@Valid @RequestBody final ContactCreateRequest request) { |
| ContactController | GET | /api/v1/contacts | public ApiResponse<List<ContactResponse>> listContacts( |
| ContactController | GET | /api/v1/contacts/{id} | public ApiResponse<ContactResponse> getContact(@PathVariable final String id) { |
| ContactController | PATCH | /api/v1/contacts/{id} | public ApiResponse<ContactResponse> updateContact(@PathVariable final String id, |
| ContactController | DELETE | /api/v1/contacts/{id} | public void deleteContact(@PathVariable final String id) { |
| ContactController | GET | /api/v1/contacts/{id}/replies | public ApiResponse<List<ContactReplyResponse>> listReplies(@PathVariable final String id) { |
| ContactController | POST | /api/v1/contacts/{id}/replies | public ApiResponse<ContactReplyResponse> addReply(@PathVariable final String id, |
| ContractTypeController | GET | /api/v1/contract-types | public ApiResponse<List<ContractTypeResponse>> listContractTypes() { |
| ContractTypeController | POST | /api/v1/contract-types | public ApiResponse<ContractTypeResponse> createContractType(@Valid @RequestBody final ContractTypeCreateRequest request) { |
| ContractTypeController | GET | /api/v1/contract-types/{id} | public ApiResponse<ContractTypeResponse> getContractType(@PathVariable final String id) { |
| ContractTypeController | PATCH | /api/v1/contract-types/{id} | public ApiResponse<ContractTypeResponse> updateContractType(@PathVariable final String id, |
| ContractTypeController | DELETE | /api/v1/contract-types/{id} | public void deleteContractType(@PathVariable final String id) { |
| DashboardController | GET | /api/v1/dashboard/summary | public ApiResponse<DashboardSummaryResponse> getSummary() { |
| DashboardController | GET | /api/v1/dashboard/notifications | public ApiResponse<List<DashboardNotification>> getNotifications() { |
| DocumentCategoryController | GET | /api/v1/document-categories | public ApiResponse<List<DocumentCategoryResponse>> listCategories() { |
| DocumentCategoryController | POST | /api/v1/document-categories | public ApiResponse<DocumentCategoryResponse> createCategory(@Valid @RequestBody final DocumentCategoryCreateRequest request) { |
| DocumentCategoryController | GET | /api/v1/document-categories/{id} | public ApiResponse<DocumentCategoryResponse> getCategory(@PathVariable final String id) { |
| DocumentCategoryController | PATCH | /api/v1/document-categories/{id} | public ApiResponse<DocumentCategoryResponse> updateCategory(@PathVariable final String id, |
| DocumentCategoryController | DELETE | /api/v1/document-categories/{id} | public void deleteCategory(@PathVariable final String id) { |
| DocumentController | GET | /api/v1/documents | public ApiResponse<List<DocumentResponse>> listDocuments( |
| DocumentController | POST | /api/v1/documents | public ApiResponse<DocumentResponse> createDocument(@Valid @RequestBody final DocumentCreateRequest request) { |
| DocumentController | POST | /api/v1/documents | public ApiResponse<DocumentResponse> uploadDocument(@RequestPart("file") final MultipartFile file, |
| DocumentController | GET | /api/v1/documents/{id} | public ApiResponse<DocumentResponse> getDocument(@PathVariable final String id) { |
| DocumentController | DELETE | /api/v1/documents/{id} | public void deleteDocument(@PathVariable final String id) { |
| DocumentController | GET | /api/v1/documents/{id}/download | public ResponseEntity<byte[]> download(@PathVariable final String id) { |
| InvoiceController | GET | /api/v1/invoices | public ApiResponse<List<InvoiceResponse>> listInvoices( |
| InvoiceController | POST | /api/v1/invoices | public ApiResponse<InvoiceResponse> createInvoice(@Valid @RequestBody final InvoiceCreateRequest request) { |
| InvoiceController | GET | /api/v1/invoices/{id} | public ApiResponse<InvoiceResponse> getInvoice(@PathVariable final String id) { |
| InvoiceController | PATCH | /api/v1/invoices/{id} | public ApiResponse<InvoiceResponse> updateInvoice(@PathVariable final String id, |
| InvoiceController | DELETE | /api/v1/invoices/{id} | public void deleteInvoice(@PathVariable final String id) { |
| InvoiceController | POST | /api/v1/invoices/{id}/status | public ApiResponse<InvoiceResponse> updateStatus(@PathVariable final String id, |
| InvoiceController | POST | /api/v1/invoices/export | public ResponseEntity<byte[]> exportInvoices(@RequestBody final InvoiceExportRequest request) { |
| ProjectController | GET | /api/v1/projects | public ApiResponse<List<ProjectResponse>> listProjects( |
| ProjectController | POST | /api/v1/projects | public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody final ProjectCreateRequest request) { |
| ProjectController | GET | /api/v1/projects/{id} | public ApiResponse<ProjectResponse> getProject(@PathVariable final String id) { |
| ProjectController | PATCH | /api/v1/projects/{id} | public ApiResponse<ProjectResponse> updateProject(@PathVariable final String id, |
| ProjectController | DELETE | /api/v1/projects/{id} | public void deleteProject(@PathVariable final String id) { |
| ProjectController | GET | /api/v1/projects/{projectId}/client | public ApiResponse<ClientResponse> getClientByProjectId(@PathVariable final String projectId) { |
| ProjectController | GET | /api/v1/projects/{projectId}/documents | public ApiResponse<List<DocumentResponse>> getDocumentsByProjectId(@PathVariable final String projectId, |
| ProjectController | GET | /api/v1/projects/{projectId}/planning | public ApiResponse<List<ScheduleResponse>> getPlanningByProjectId(@PathVariable final String projectId, |
| ProjectController | GET | /api/v1/projects/{projectId}/invoices | public ApiResponse<List<InvoiceResponse>> getInvoicesByProjectId(@PathVariable final String projectId, |
| ProjectController | GET | /api/v1/projects/{projectId}/finances | public ApiResponse<FinancialsResponse> getFinancesByProjectId(@PathVariable final String projectId) { |
| ProjectTypeController | GET | /api/v1/project-types | public ApiResponse<List<ProjectTypeResponse>> listProjectTypes() { |
| ProjectTypeController | POST | /api/v1/project-types | public ApiResponse<ProjectTypeResponse> createProjectType(@Valid @RequestBody final ProjectTypeCreateRequest request) { |
| ProjectTypeController | GET | /api/v1/project-types/{id} | public ApiResponse<ProjectTypeResponse> getProjectType(@PathVariable final String id) { |
| ProjectTypeController | PATCH | /api/v1/project-types/{id} | public ApiResponse<ProjectTypeResponse> updateProjectType(@PathVariable final String id, |
| ProjectTypeController | DELETE | /api/v1/project-types/{id} | public void deleteProjectType(@PathVariable final String id) { |
| ReportsController | GET | /api/v1/reports/overview | public ApiResponse<ReportsOverviewResponse> getOverview() { |
| ReportsController | GET | /api/v1/reports/projects | public ApiResponse<ProjectsReportResponse> getProjectsReport() { |
| ReportsController | GET | /api/v1/reports/margins | public ApiResponse<MarginsReportResponse> getMarginsReport() { |
| ReportsController | GET | /api/v1/reports/invoices | public ApiResponse<InvoicesReportResponse> getInvoicesReport() { |
| ScheduleController | GET | /api/v1/schedules | public ApiResponse<List<ScheduleResponse>> listSchedules( |
| ScheduleController | POST | /api/v1/schedules | public ApiResponse<ScheduleResponse> createSchedule(@Valid @RequestBody final ScheduleCreateRequest request) { |
| ScheduleController | GET | /api/v1/schedules/{id} | public ApiResponse<ScheduleResponse> getSchedule(@PathVariable final String id) { |
| ScheduleController | PATCH | /api/v1/schedules/{id} | public ApiResponse<ScheduleResponse> updateSchedule(@PathVariable final String id, |
| ScheduleController | DELETE | /api/v1/schedules/{id} | public void deleteSchedule(@PathVariable final String id) { |
| ScheduleController | GET | /api/v1/schedules/export | public ResponseEntity<byte[]> exportSchedules( |
| SettingsController | GET | /api/v1/settings | public ApiResponse<SettingsResponse> getSettings() { |
| SettingsController | PATCH | /api/v1/settings | public ApiResponse<SettingsResponse> patchSettings(@Valid @RequestBody final SettingsPatchRequest request) { |
| UserController | PATCH | /api/v1/users/me | public void updateProfile( |
| UserController | POST | /api/v1/users/me/password | public void changePassword( |
| UserController | PATCH | /api/v1/users/me/deactivate | public void deactivateAccount(final Authentication principal) { |
| UserController | PATCH | /api/v1/users/me/reactivate | public void reactivateAccount(final Authentication principal) { |
| UserController | DELETE | /api/v1/users/me | public void deleteAccount(final Authentication principal) { |

---

## 19) Notes & Known Issues

- `SecurityConfig permits /api/v1/** publicly; endpoints appear effectively public.`
- `OpenAPI file .tmp_openapi.json may be stale relative to current controllers.`

---

## 20) How This Doc Was Generated

- **Source of truth:** `Source code in this repository`
- **Extraction method:** `Static parsing of Java files and pom/application.yml`
- **Last updated:** `2026-02-09`

---

**End of documentation.**