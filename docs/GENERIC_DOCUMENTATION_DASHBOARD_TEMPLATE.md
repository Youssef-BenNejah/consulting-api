# 📚 GENERIC API DOCUMENTATION DASHBOARD TEMPLATE

> **Version**: 1.0.0  
> **Purpose**: This is a **hyper-detailed, generic template** for generating a complete API documentation dashboard in Lovable. Copy this file and fill in the sections with your project's data.

---

## 🎯 WHAT THIS TEMPLATE CREATES

This template generates a **full-featured documentation dashboard** with the following sections:

1. **Overview** - Hero section with project stats
2. **Full Documentation** - Complete markdown documentation rendered beautifully
3. **API Reference** - Detailed endpoint documentation with request/response examples
4. **Scrum Board** - Epics → Features → Stories → Tickets with checkboxes
5. **Use Cases** - Test cases with validation, flow diagrams, and notes
6. **Endpoints View** - All REST endpoints grouped by controller
7. **Models View** - Database models with all fields
8. **DTOs View** - Data Transfer Objects (input/output)
9. **WebSocket View** - Real-time communication topics

---

## 🏗️ ARCHITECTURE OVERVIEW

### Tech Stack
- **Framework**: React + TypeScript + Vite
- **Styling**: Tailwind CSS with semantic design tokens
- **UI Components**: shadcn/ui (Card, Badge, Tabs, Accordion, etc.)
- **Animations**: Framer Motion
- **Icons**: Lucide React
- **State**: React useState + localStorage for persistence

### File Structure
```
src/
├── components/
│   ├── Sidebar.tsx              # Navigation sidebar
│   ├── HeroSection.tsx          # Hero banner with stats
│   ├── OverviewSection.tsx      # Overview content
│   ├── EndpointsView.tsx        # Endpoints display
│   ├── ModelsView.tsx           # Models/DTOs display
│   ├── WebSocketView.tsx        # WebSocket topics
│   ├── DocumentationView.tsx    # Full docs renderer
│   ├── APIReferenceView.tsx     # API reference section
│   ├── ScrumBoardView.tsx       # Epics/Features/Tickets
│   ├── UseCasesView.tsx         # Test cases validation
│   ├── SearchBar.tsx            # Search component
│   ├── MethodFilter.tsx         # HTTP method filter
│   ├── HttpMethodBadge.tsx      # Method badge component
│   └── ui/                      # shadcn components
├── data/
│   ├── apiData.ts               # Endpoints, Models, DTOs, WebSocket, Docs
│   ├── scrumData.ts             # Epics, Features, Stories, Tickets
│   └── useCasesData.ts          # Test cases data
├── pages/
│   └── Index.tsx                # Main page with routing
├── index.css                    # Theme and styles
└── App.tsx                      # App wrapper
```

---

## 📝 SECTION 1: PROJECT METADATA

Replace the placeholders with your project information:

```typescript
// PROJECT METADATA
const PROJECT = {
  name: "{{PROJECT_NAME}}",           // e.g., "Tictak API"
  tagline: "{{PROJECT_TAGLINE}}",     // e.g., "Logistics Platform Backend"
  version: "{{VERSION}}",             // e.g., "1.0.0"
  baseUrl: "{{BASE_URL}}",            // e.g., "https://api.example.com"
  description: "{{DESCRIPTION}}",     // Short description
};
```

---

## 📝 SECTION 2: DESIGN SYSTEM (THEME)

### Color Palette (index.css)

The theme uses HSL values for all colors. Define these CSS variables:

```css
:root {
  /* Base Colors */
  --background: {{H}} {{S}}% {{L}}%;     /* e.g., 222 47% 6% (dark background) */
  --foreground: {{H}} {{S}}% {{L}}%;     /* e.g., 210 40% 98% (light text) */

  /* Card */
  --card: {{H}} {{S}}% {{L}}%;           /* Slightly lighter than background */
  --card-foreground: {{H}} {{S}}% {{L}}%;

  /* Primary (main accent color) */
  --primary: {{H}} {{S}}% {{L}}%;        /* e.g., 199 89% 48% (cyan) */
  --primary-foreground: {{H}} {{S}}% {{L}}%;

  /* Secondary */
  --secondary: {{H}} {{S}}% {{L}}%;
  --secondary-foreground: {{H}} {{S}}% {{L}}%;

  /* Muted (subdued elements) */
  --muted: {{H}} {{S}}% {{L}}%;
  --muted-foreground: {{H}} {{S}}% {{L}}%;

  /* Accent (secondary accent) */
  --accent: {{H}} {{S}}% {{L}}%;         /* e.g., 262 83% 58% (purple) */
  --accent-foreground: {{H}} {{S}}% {{L}}%;

  /* Destructive (errors, delete) */
  --destructive: 0 84% 60%;              /* Red */
  --destructive-foreground: 210 40% 98%;

  /* Border and Input */
  --border: {{H}} {{S}}% {{L}}%;
  --input: {{H}} {{S}}% {{L}}%;
  --ring: {{H}} {{S}}% {{L}}%;           /* Focus ring color */

  /* Border Radius */
  --radius: 0.75rem;

  /* HTTP Method Colors (REQUIRED) */
  --http-get: 187 92% 69%;               /* Cyan/Teal */
  --http-post: 142 71% 45%;              /* Green */
  --http-put: 38 92% 50%;                /* Orange */
  --http-patch: 262 83% 58%;             /* Purple */
  --http-delete: 0 84% 60%;              /* Red */

  /* Sidebar */
  --sidebar-background: {{H}} {{S}}% {{L}}%;
  --sidebar-foreground: {{H}} {{S}}% {{L}}%;
  --sidebar-accent: {{H}} {{S}}% {{L}}%;
  --sidebar-border: {{H}} {{S}}% {{L}}%;
}
```

### Typography (fonts)

```css
/* Import fonts */
@import url('https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600;700&family=Inter:wght@300;400;500;600;700;800&display=swap');

body {
  font-family: 'Inter', sans-serif;
}

code, pre, .font-mono {
  font-family: 'JetBrains Mono', monospace;
}
```

---

## 📝 SECTION 3: ENDPOINTS DATA

Define all your API endpoints in this format:

```typescript
// src/data/apiData.ts

export interface Endpoint {
  controller: string;                    // Controller/Module name
  http: 'Get' | 'Post' | 'Put' | 'Delete' | 'Patch';
  path: string;                          // API path
  signature: string;                     // Method signature
  description?: string;                  // Human-readable description
  auth?: string;                         // Required roles (e.g., "ADMIN, USER")
  request?: string;                      // Request DTO or body format
  response?: string;                     // Response type
}

export const endpoints: Endpoint[] = [
  // ============ EXAMPLE ENDPOINTS ============
  
  // Authentication Controller
  {
    controller: "AuthController",
    http: "Post",
    path: "/auth/register",
    signature: "public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request)",
    description: "Register a new user",
    request: "RegisterRequest",
    response: "User"
  },
  {
    controller: "AuthController",
    http: "Post",
    path: "/auth/login",
    signature: "public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request)",
    description: "User login with email/password",
    request: "LoginRequest",
    response: "AuthResponse"
  },
  {
    controller: "AuthController",
    http: "Post",
    path: "/auth/refresh",
    signature: "public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request)",
    description: "Refresh access token",
    request: "RefreshRequest",
    response: "AuthResponse"
  },
  {
    controller: "AuthController",
    http: "Delete",
    path: "/auth/delete-account",
    signature: "@PreAuthorize(\"hasRole('USER')\")",
    description: "Delete user account",
    auth: "USER",
    response: "{ message: string }"
  },

  // User Controller
  {
    controller: "UserController",
    http: "Get",
    path: "/users",
    signature: "public ResponseEntity<List<User>> getAllUsers()",
    description: "Get all users",
    auth: "ADMIN",
    response: "List<User>"
  },
  {
    controller: "UserController",
    http: "Get",
    path: "/users/{id}",
    signature: "public ResponseEntity<User> getUser(@PathVariable String id)",
    description: "Get user by ID",
    auth: "ADMIN, USER",
    response: "User"
  },
  {
    controller: "UserController",
    http: "Patch",
    path: "/users/{id}",
    signature: "public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request)",
    description: "Update user profile",
    auth: "USER",
    request: "UpdateUserRequest",
    response: "User"
  },

  // Add all your endpoints following this pattern...
  // Group by controller for better organization
];
```

---

## 📝 SECTION 4: MODELS DATA

Define all database/domain models:

```typescript
// src/data/apiData.ts

export interface Field {
  type: string;                          // Field type (String, int, boolean, Date, etc.)
  name: string;                          // Field name
}

export interface Model {
  file: string;                          // Source file path
  name: string;                          // Model name
  fields: Field[];                       // List of fields
}

export const models: Model[] = [
  // ============ EXAMPLE MODELS ============
  
  {
    file: "src/models/User.java",
    name: "User",
    fields: [
      { name: "id", type: "String" },
      { name: "email", type: "String" },
      { name: "firstName", type: "String" },
      { name: "lastName", type: "String" },
      { name: "password", type: "String" },
      { name: "role", type: "Role" },
      { name: "phoneNumber", type: "String" },
      { name: "createdAt", type: "Date" },
      { name: "updatedAt", type: "Date" },
      { name: "banned", type: "boolean" },
      { name: "emailVerified", type: "boolean" },
    ]
  },
  {
    file: "src/models/Product.java",
    name: "Product",
    fields: [
      { name: "id", type: "String" },
      { name: "name", type: "String" },
      { name: "description", type: "String" },
      { name: "price", type: "double" },
      { name: "quantity", type: "int" },
      { name: "category", type: "Category" },
      { name: "images", type: "List<String>" },
      { name: "createdAt", type: "Date" },
    ]
  },
  {
    file: "src/models/Order.java",
    name: "Order",
    fields: [
      { name: "id", type: "String" },
      { name: "userId", type: "String" },
      { name: "items", type: "List<OrderItem>" },
      { name: "total", type: "double" },
      { name: "status", type: "OrderStatus" },
      { name: "createdAt", type: "Date" },
      { name: "shippingAddress", type: "Address" },
    ]
  },

  // Add all your models...
];
```

---

## 📝 SECTION 5: DTOs DATA

Define all Data Transfer Objects (input/output):

```typescript
// src/data/apiData.ts

export interface DTO {
  file: string;                          // Source file path
  name: string;                          // DTO name
  fields: Field[];                       // List of fields
  category: 'in' | 'out';                // Input or Output DTO
}

export const dtos: DTO[] = [
  // ============ INPUT DTOs ============
  
  {
    file: "src/dto/in/RegisterRequest.java",
    name: "RegisterRequest",
    category: "in",
    fields: [
      { name: "email", type: "@Email String" },
      { name: "password", type: "@NotBlank String" },
      { name: "firstName", type: "@NotBlank String" },
      { name: "lastName", type: "@NotBlank String" },
      { name: "phoneNumber", type: "String" },
    ]
  },
  {
    file: "src/dto/in/LoginRequest.java",
    name: "LoginRequest",
    category: "in",
    fields: [
      { name: "email", type: "@Email String" },
      { name: "password", type: "@NotBlank String" },
    ]
  },
  {
    file: "src/dto/in/UpdateUserRequest.java",
    name: "UpdateUserRequest",
    category: "in",
    fields: [
      { name: "firstName", type: "String" },
      { name: "lastName", type: "String" },
      { name: "phoneNumber", type: "String" },
      { name: "avatar", type: "String" },
    ]
  },

  // ============ OUTPUT DTOs ============
  
  {
    file: "src/dto/out/AuthResponse.java",
    name: "AuthResponse",
    category: "out",
    fields: [
      { name: "accessToken", type: "String" },
      { name: "refreshToken", type: "String" },
      { name: "user", type: "User" },
      { name: "expiresIn", type: "long" },
    ]
  },
  {
    file: "src/dto/out/UserSummaryDTO.java",
    name: "UserSummaryDTO",
    category: "out",
    fields: [
      { name: "id", type: "String" },
      { name: "email", type: "String" },
      { name: "fullName", type: "String" },
      { name: "avatar", type: "String" },
    ]
  },

  // Add all your DTOs...
];
```

---

## 📝 SECTION 6: WEBSOCKET TOPICS

Define real-time WebSocket topics:

```typescript
// src/data/apiData.ts

export interface WebSocketTopic {
  topic: string;                         // Topic path
  description: string;                   // What this topic does
  type: 'subscribe' | 'publish';         // Subscribe or Publish
}

export const websocketTopics: WebSocketTopic[] = [
  // ============ SUBSCRIBE TOPICS ============
  
  {
    topic: "/topic/notifications/{userId}",
    description: "Real-time notifications for a user",
    type: "subscribe"
  },
  {
    topic: "/topic/orders/{orderId}",
    description: "Real-time order status updates",
    type: "subscribe"
  },
  {
    topic: "/topic/chat/{roomId}",
    description: "Chat messages in a room",
    type: "subscribe"
  },
  {
    topic: "/topic/user-status",
    description: "User online/offline status broadcast",
    type: "subscribe"
  },

  // ============ PUBLISH TOPICS ============
  
  {
    topic: "/app/message.send",
    description: "Send a chat message",
    type: "publish"
  },
  {
    topic: "/app/user.status",
    description: "Update user status",
    type: "publish"
  },
  {
    topic: "/app/location.update",
    description: "Update user location",
    type: "publish"
  },

  // Add all your WebSocket topics...
];
```

---

## 📝 SECTION 7: DOCUMENTATION SECTIONS

Define your full documentation content:

```typescript
// src/data/apiData.ts

export interface DocSection {
  id: string;                            // Unique section ID
  title: string;                         // Section title
  content: string;                       // Markdown content
}

export const documentationSections: DocSection[] = [
  {
    id: "overview",
    title: "Overview",
    content: `
## Overview

{{PROJECT_NAME}} is a {{PROJECT_DESCRIPTION}}.

### Features
- Feature 1
- Feature 2
- Feature 3

### Base URL
\`\`\`
{{BASE_URL}}
\`\`\`
    `
  },
  {
    id: "authentication",
    title: "Authentication",
    content: `
## Authentication

The API uses JWT (JSON Web Tokens) for authentication.

### Flow
1. User registers or logs in
2. Server returns access token + refresh token
3. Client includes token in Authorization header
4. Token expires → use refresh endpoint

### Headers
\`\`\`
Authorization: Bearer <access_token>
\`\`\`

### Roles
- \`ADMIN\` - Full access
- \`USER\` - Standard user access
- \`GUEST\` - Limited access
    `
  },
  {
    id: "error-handling",
    title: "Error Handling",
    content: `
## Error Handling

All errors follow this format:

\`\`\`json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users",
  "code": "VALIDATION_ERROR"
}
\`\`\`

### Common Error Codes
| Code | Description |
|------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Invalid/missing token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Server Error - Internal error |
    `
  },

  // Add all your documentation sections...
];
```

---

## 📝 SECTION 8: SCRUM BOARD DATA (Epics/Features/Tickets)

Define your project breakdown:

```typescript
// src/data/scrumData.ts

export interface Ticket {
  id: string;                            // Ticket ID (e.g., "AUTH-101")
  title: string;                         // Short title
  endpoint?: string;                     // Related endpoint
  requestDto?: string;                   // Request DTO
  successCode?: string;                  // Success response
  errorCodes?: string[];                 // Error codes
  validation?: string[];                 // Validation rules
  dataTouched?: string;                  // Affected data
  acceptance?: string;                   // Acceptance criteria
  tests?: string[];                      // Test cases
  rules?: string;                        // Business rules
}

export interface Story {
  title: string;                         // Story title
  tickets: Ticket[];                     // Related tickets
}

export interface Feature {
  id: string;                            // Feature ID (e.g., "1.1")
  title: string;                         // Feature title
  stories?: Story[];                     // Optional: User stories
  tickets?: Ticket[];                    // Direct tickets (if no stories)
}

export interface Epic {
  id: number;                            // Epic number
  title: string;                         // Epic title
  icon: string;                          // Lucide icon name
  color: string;                         // Color theme (http-get, http-post, etc.)
  features: Feature[];                   // Features in this epic
}

export interface GlobalContract {
  id: string;                            // Contract ID
  title: string;                         // Contract title
  description: string;                   // Description
  items: string[];                       // Contract items
}

// ============ GLOBAL CONTRACTS ============

export const globalContracts: GlobalContract[] = [
  {
    id: "0.1",
    title: "Error Response Format",
    description: "Standard error response structure:",
    items: [
      "timestamp (ISO instant)",
      "status (HTTP status)",
      "error (reason phrase)",
      "message (human message)",
      "path (request path)",
      "code (optional error code)",
    ]
  },
  {
    id: "0.2",
    title: "Authentication Model",
    description: "JWT-based authentication:",
    items: [
      "Access token in Authorization: Bearer <token>",
      "Refresh token in body for /auth/refresh",
      "Roles: ADMIN, USER, GUEST",
    ]
  },
];

// ============ EPICS ============

export const epics: Epic[] = [
  {
    id: 1,
    title: "Authentication & Identity",
    icon: "Shield",                      // Lucide icon name
    color: "http-post",                  // Color theme
    features: [
      {
        id: "1.1",
        title: "Email/Password Authentication",
        stories: [
          {
            title: "User Registration",
            tickets: [
              {
                id: "AUTH-101",
                title: "Register User",
                endpoint: "POST /auth/register",
                requestDto: "RegisterRequest",
                successCode: "201 + User",
                errorCodes: ["400", "409"],
                validation: ["email", "password", "firstName", "lastName"],
                dataTouched: "users table",
                acceptance: "unique email, hashed password, role=USER",
                tests: ["success", "duplicate email", "invalid payload"],
              },
            ],
          },
          {
            title: "User Login",
            tickets: [
              {
                id: "AUTH-102",
                title: "Login",
                endpoint: "POST /auth/login",
                requestDto: "LoginRequest",
                successCode: "200 + AuthResponse",
                errorCodes: ["401"],
                rules: "reject banned users",
                tests: ["success", "wrong password", "banned user"],
              },
              {
                id: "AUTH-103",
                title: "Refresh Token",
                endpoint: "POST /auth/refresh",
                requestDto: "RefreshRequest",
                successCode: "200 + new tokens",
                errorCodes: ["401"],
              },
            ],
          },
        ],
      },
      {
        id: "1.2",
        title: "Social Authentication",
        tickets: [
          { id: "AUTH-201", title: "Google OAuth", endpoint: "POST /auth/google" },
          { id: "AUTH-202", title: "Apple Sign-In", endpoint: "POST /auth/apple" },
          { id: "AUTH-203", title: "Facebook Login", endpoint: "POST /auth/facebook" },
        ],
      },
      {
        id: "1.3",
        title: "Password Reset",
        tickets: [
          { id: "AUTH-301", title: "Forgot Password", endpoint: "POST /auth/forgot-password" },
          { id: "AUTH-302", title: "Verify OTP", endpoint: "POST /auth/verify-otp" },
          { id: "AUTH-303", title: "Reset Password", endpoint: "POST /auth/reset-password" },
        ],
      },
    ],
  },
  {
    id: 2,
    title: "User Management",
    icon: "UserCog",
    color: "http-patch",
    features: [
      {
        id: "2.1",
        title: "Profile Management",
        tickets: [
          { id: "USER-101", title: "Get Profile", endpoint: "GET /users/me" },
          { id: "USER-102", title: "Update Profile", endpoint: "PATCH /users/me" },
          { id: "USER-103", title: "Change Password", endpoint: "POST /users/change-password" },
          { id: "USER-104", title: "Upload Avatar", endpoint: "POST /users/avatar" },
        ],
      },
    ],
  },
  {
    id: 3,
    title: "Products & Catalog",
    icon: "Package",
    color: "http-get",
    features: [
      {
        id: "3.1",
        title: "Product CRUD",
        tickets: [
          { id: "PROD-101", title: "List Products", endpoint: "GET /products" },
          { id: "PROD-102", title: "Get Product", endpoint: "GET /products/{id}" },
          { id: "PROD-103", title: "Create Product", endpoint: "POST /products" },
          { id: "PROD-104", title: "Update Product", endpoint: "PUT /products/{id}" },
          { id: "PROD-105", title: "Delete Product", endpoint: "DELETE /products/{id}" },
        ],
      },
    ],
  },

  // Add all your epics...
];

// ============ STATISTICS ============

export const scrumStats = {
  totalEpics: epics.length,
  totalFeatures: epics.reduce((acc, e) => acc + e.features.length, 0),
  totalTickets: epics.reduce((acc, e) => 
    acc + e.features.reduce((a, f) => {
      const featureTickets = f.tickets?.length || 0;
      const storyTickets = f.stories?.reduce((s, st) => s + st.tickets.length, 0) || 0;
      return a + featureTickets + storyTickets;
    }, 0), 0),
  totalEndpoints: /* count from endpoints array */,
};
```

### Available Icons (Lucide)
Use any of these icon names in the `icon` field:
- `Shield` - Security/Auth
- `UserCog` - User management
- `Calculator` - Calculations
- `Route` - Navigation/Routes
- `CreditCard` - Payments
- `Wifi` - Real-time/WebSocket
- `Bell` - Notifications
- `Building2` - Admin/Organizations
- `Briefcase` - Business
- `MessageSquareWarning` - Feedback/Reports
- `Package` - Products/Items
- `Camera` - Media
- `Clock` - Scheduled tasks
- `ShieldCheck` - Security hardening

### Available Colors
Use any of these in the `color` field:
- `http-get` - Cyan/Teal
- `http-post` - Green
- `http-put` - Orange
- `http-patch` - Purple
- `http-delete` - Red
- `primary` - Primary theme color
- `accent` - Accent theme color

---

## 📝 SECTION 9: USE CASES / TEST CASES

Define test cases for validation:

```typescript
// src/data/useCasesData.ts

export type Priority = "critique" | "haute" | "moyenne" | "basse";

export type Category = 
  | "auth" 
  | "profile" 
  | "products" 
  | "orders" 
  | "payment" 
  | "websocket" 
  | "notification" 
  | "admin" 
  | "security";

export interface UseCase {
  id: string;                            // Test case ID (e.g., "TC-001")
  title: string;                         // Short title
  description: string;                   // What this test verifies
  expectedResult: string;                // Expected outcome
  category: Category;                    // Category for filtering
  priority: Priority;                    // Importance level
  flow: string[];                        // Step-by-step flow
  relatedTickets?: string[];             // Related ticket IDs
  relatedEndpoints?: string[];           // Related endpoints
}

export const categoryConfig: Record<Category, { label: string; icon: string; color: string }> = {
  auth: { label: "Authentication", icon: "Shield", color: "http-post" },
  profile: { label: "Profile", icon: "UserCog", color: "http-patch" },
  products: { label: "Products", icon: "Package", color: "http-get" },
  orders: { label: "Orders", icon: "ShoppingCart", color: "http-put" },
  payment: { label: "Payment", icon: "CreditCard", color: "accent" },
  websocket: { label: "WebSocket", icon: "Wifi", color: "primary" },
  notification: { label: "Notification", icon: "Bell", color: "http-post" },
  admin: { label: "Admin", icon: "Building2", color: "http-delete" },
  security: { label: "Security", icon: "ShieldCheck", color: "http-delete" },
};

export const priorityConfig: Record<Priority, { label: string; color: string }> = {
  critique: { label: "Critical", color: "bg-red-500 text-white" },
  haute: { label: "High", color: "bg-orange-500 text-white" },
  moyenne: { label: "Medium", color: "bg-yellow-500 text-black" },
  basse: { label: "Low", color: "bg-gray-400 text-white" },
};

export const useCases: UseCase[] = [
  // ============ AUTHENTICATION TESTS ============
  
  {
    id: "TC-001",
    title: "User registration with valid email",
    description: "Verify a new user can register with valid email and password.",
    expectedResult: "201 Created + User object with role=USER",
    category: "auth",
    priority: "critique",
    flow: [
      "User navigates to registration page",
      "Fills form (email, password, firstName, lastName)",
      "Submits form",
      "System validates fields",
      "System creates user in database",
      "User receives confirmation + User object",
    ],
    relatedTickets: ["AUTH-101"],
    relatedEndpoints: ["POST /auth/register"],
  },
  {
    id: "TC-002",
    title: "Registration with duplicate email → Failure",
    description: "Verify system rejects registration with existing email.",
    expectedResult: "409 Conflict + error message",
    category: "auth",
    priority: "haute",
    flow: [
      "User tries to register with existing email",
      "System checks email uniqueness",
      "System rejects with 409 error",
    ],
    relatedTickets: ["AUTH-101"],
    relatedEndpoints: ["POST /auth/register"],
  },
  {
    id: "TC-003",
    title: "Login with valid credentials",
    description: "Verify user can login with correct credentials.",
    expectedResult: "200 OK + AuthResponse with tokens",
    category: "auth",
    priority: "critique",
    flow: [
      "User navigates to login page",
      "Enters email and password",
      "Submits form",
      "System validates credentials",
      "System generates JWT tokens",
      "User receives AuthResponse",
    ],
    relatedTickets: ["AUTH-102"],
    relatedEndpoints: ["POST /auth/login"],
  },
  {
    id: "TC-004",
    title: "Protected endpoint without token → 401",
    description: "Verify protected endpoints reject unauthenticated requests.",
    expectedResult: "401 Unauthorized",
    category: "security",
    priority: "critique",
    flow: [
      "Client sends request without Authorization header",
      "System checks for token",
      "System rejects with 401",
    ],
    relatedTickets: ["SEC-101"],
    relatedEndpoints: [],
  },

  // Add all your test cases...
];

export const useCaseStats = {
  total: useCases.length,
  byCategory: Object.entries(
    useCases.reduce((acc, uc) => {
      acc[uc.category] = (acc[uc.category] || 0) + 1;
      return acc;
    }, {} as Record<Category, number>)
  ),
  byPriority: Object.entries(
    useCases.reduce((acc, uc) => {
      acc[uc.priority] = (acc[uc.priority] || 0) + 1;
      return acc;
    }, {} as Record<Priority, number>)
  ),
};
```

---

## 📝 SECTION 10: NAVIGATION STRUCTURE

The sidebar navigation is defined in `Sidebar.tsx`:

```typescript
// src/components/Sidebar.tsx

export type ViewType = 
  | "overview" 
  | "endpoints" 
  | "models" 
  | "dtos" 
  | "websocket" 
  | "documentation" 
  | "api-reference" 
  | "scrum-board" 
  | "use-cases";

const navItems = [
  { id: "overview", label: "Overview", icon: Home },
  { id: "documentation", label: "Full Docs", icon: BookOpen },
  { id: "api-reference", label: "API Reference", icon: Layers },
  { id: "scrum-board", label: "Scrum Board", icon: Kanban },
  { id: "use-cases", label: "Use Cases", icon: ListChecks },
  { id: "endpoints", label: "Endpoints", icon: Layers },
  { id: "models", label: "Models", icon: Database },
  { id: "dtos", label: "DTOs", icon: FileJson },
  { id: "websocket", label: "WebSocket", icon: Wifi },
];
```

---

## 📝 SECTION 11: FEATURES SUMMARY

### 1. Hero Section
- Project name, tagline, version
- Statistics cards (endpoints count, models count, DTOs count)
- Gradient background with animations

### 2. Search & Filter
- Global search across all sections
- HTTP method filter (GET, POST, PUT, PATCH, DELETE)
- Category filters for use cases

### 3. Endpoints View
- Grouped by controller
- Collapsible accordion
- HTTP method badges with colors
- Path, signature, description, auth requirements

### 4. Models/DTOs View
- Tabbed interface (Models | DTOs)
- Field type and name display
- Source file reference
- Search functionality

### 5. WebSocket View
- Subscribe vs Publish topics
- Topic paths with descriptions
- Real-time indicator

### 6. Documentation View
- Full markdown rendering
- Section navigation
- Code block highlighting
- Search through sections

### 7. API Reference View
- Detailed endpoint documentation
- Request/Response examples
- Authorization requirements
- Error codes

### 8. Scrum Board
- Epics with icons and colors
- Features collapsible
- Stories with tickets
- **Checkbox validation** with localStorage persistence
- Progress tracking per Epic/Feature
- Global progress bar

### 9. Use Cases
- Test case cards with priority badges
- **Checkbox validation** with localStorage
- **Notes/observations** textarea (saved to localStorage)
- Flow steps visualization
- Category and priority filters
- Progress bar
- Related tickets and endpoints links

---

## 📝 SECTION 12: PERSISTENCE (localStorage)

The following data is persisted in localStorage:

```typescript
// Scrum Board - Validated tickets
localStorage.setItem('scrum-checked-tickets', JSON.stringify([...checkedTicketIds]));

// Use Cases - Validated test cases
localStorage.setItem('usecases-validated', JSON.stringify([...validatedCaseIds]));

// Use Cases - Notes for each test case
localStorage.setItem('usecases-notes', JSON.stringify({ 
  "TC-001": "Tested on Chrome, works fine",
  "TC-002": "Needs review",
  // ...
}));
```

---

## 🚀 HOW TO USE THIS TEMPLATE

### Step 1: Create a new Lovable project
1. Go to Lovable
2. Create a new project
3. Paste this prompt:

```
Create a professional API documentation dashboard based on this template.
Use the dark developer console aesthetic (Stripe/Linear inspired).
Include all sections: Overview, Full Docs, API Reference, Scrum Board, 
Use Cases, Endpoints, Models, DTOs, WebSocket.

Here is my project data:
[PASTE YOUR FILLED TEMPLATE HERE]
```

### Step 2: Fill in your data
Replace all `{{PLACEHOLDERS}}` and example data with your actual project information:
- Project metadata
- Endpoints
- Models
- DTOs
- WebSocket topics
- Documentation sections
- Epics/Features/Tickets
- Use cases

### Step 3: Customize the theme
Modify the color palette in the CSS variables to match your brand.

### Step 4: Add more sections (optional)
The structure is modular - you can add new views by:
1. Adding a new ViewType to the Sidebar
2. Creating a new component
3. Adding the case to Index.tsx

---

## 📋 CHECKLIST

Before generating, ensure you have:

- [ ] Project name and description
- [ ] All endpoints with paths and methods
- [ ] All models with fields
- [ ] All DTOs (input and output)
- [ ] WebSocket topics (if any)
- [ ] Documentation sections
- [ ] Epics and features breakdown
- [ ] Tickets with details
- [ ] Test cases / use cases
- [ ] Color theme preferences

---

## 📞 SUPPORT

This template was created based on a real production documentation dashboard.
It supports:
- **80+ endpoints**
- **23+ models**
- **27+ DTOs**
- **12+ WebSocket topics**
- **14+ Epics**
- **56+ Tickets**
- **42+ Test cases**

Scale as needed for your project!

---

**END OF TEMPLATE**
