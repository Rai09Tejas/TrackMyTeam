# TrackMyTeam Backend

A task tracking system built with Spring Boot, featuring JWT authentication, task management, and email notifications.

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for database operations
- **H2 Database** (in-memory) for development
- **Lombok** for reducing boilerplate code
- **Spring Mail** for email notifications

## Database

The application uses **H2 in-memory database** for easy development and testing without requiring MySQL installation.

- **H2 Console**: Access at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:trackmyteam`
  - Username: `sa`
  - Password: (leave empty)

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Steps to Run

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints (No authentication required)

#### 1. Register a New User
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** JWT Token (String)
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTczMzE2MjQwMCwiZXhwIjoxNzMzMjQ4ODAwfQ...
```

#### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:** JWT Token (String)
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTczMzE2MjQwMCwiZXhwIjoxNzMzMjQ4ODAwfQ...
```

---

### Task Management Endpoints (Authentication required)

**Note**: All task endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

#### 3. Create a Task
```bash
POST http://localhost:8080/api/tasks
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the TrackMyTeam project",
  "status": "PENDING",
  "deadline": "2025-12-15T10:00:00"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the TrackMyTeam project",
  "deadline": "2025-12-15T10:00:00",
  "status": "PENDING",
  "assignedUser": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### 4. Get All My Tasks
```bash
GET http://localhost:8080/api/tasks
Authorization: Bearer <your-jwt-token>
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation",
    "deadline": "2025-12-15T10:00:00",
    "status": "PENDING",
    "assignedUser": {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "role": "USER"
    }
  }
]
```

#### 5. Update a Task
```bash
PUT http://localhost:8080/api/tasks/{id}
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Updated task title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "deadline": "2025-12-20T15:00:00"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Updated task title",
  "description": "Updated description",
  "deadline": "2025-12-20T15:00:00",
  "status": "IN_PROGRESS",
  "assignedUser": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### 6. Delete a Task
```bash
DELETE http://localhost:8080/api/tasks/{id}
Authorization: Bearer <your-jwt-token>
```

**Response:** 200 OK (No content)

---

## Data Models

### Task Status Values
- `PENDING` - Task is pending
- `IN_PROGRESS` - Task is in progress
- `COMPLETED` - Task is completed

### User Roles
- `USER` - Regular user
- `ADMIN` - Administrator with elevated privileges

### Task Model
```json
{
  "id": "Long (auto-generated)",
  "title": "String (required)",
  "description": "String (optional)",
  "deadline": "LocalDateTime (ISO-8601 format: yyyy-MM-ddTHH:mm:ss)",
  "status": "TaskStatus enum (PENDING, IN_PROGRESS, COMPLETED)",
  "assignedUser": "User object (auto-assigned to current user)"
}
```

### User Model
```json
{
  "id": "Long (auto-generated)",
  "username": "String (required, unique)",
  "email": "String (required, unique)",
  "password": "String (required, will be encrypted)",
  "role": "Role enum (USER, ADMIN)"
}
```

---

## Testing with PowerShell

### Quick Test Script
Run the provided test script:
```powershell
.\test-api.ps1
```

### Manual Testing with PowerShell

#### 1. Register a User
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"testuser","email":"test@example.com","password":"test123"}'
```

#### 2. Login and Save Token
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"testuser","password":"test123"}'

$token = $response.Content
Write-Host "Token: $token"
```

#### 3. Create a Task
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/tasks" `
  -Method POST `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $token"
  } `
  -Body '{"title":"My Task","description":"Task description","status":"PENDING","deadline":"2025-12-15T10:00:00"}'
```

#### 4. Get All Tasks
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/tasks" `
  -Method GET `
  -Headers @{"Authorization"="Bearer $token"}
```

---

## Testing with cURL

### Complete Flow Example

#### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"test123"}'
```

#### 2. Login to Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'
```

Save the returned token for subsequent requests.

#### 3. Create a Task (Replace `<TOKEN>` with your JWT)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"title":"My First Task","description":"Test task","status":"PENDING","deadline":"2025-12-15T10:00:00"}'
```

#### 4. Get All Tasks
```bash
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <TOKEN>"
```

#### 5. Update a Task (Replace `{id}` with task ID)
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Task","description":"Updated description","status":"IN_PROGRESS","deadline":"2025-12-20T15:00:00"}'
```

#### 6. Delete a Task
```bash
curl -X DELETE http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

## Email Notifications

The application includes an email service that sends notifications for:
- Task assignments
- Upcoming deadlines (via scheduled reminders)

**Note**: Email configuration in `application.properties` needs to be updated with valid SMTP credentials for email functionality to work.

---

## Switching to MySQL (Optional)

To use MySQL instead of H2:

### 1. Update `pom.xml`
Replace H2 dependency with MySQL:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Update `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trackmyteam?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 3. Rebuild the Application
```bash
mvn clean install
mvn spring-boot:run
```

---

## Features

✅ User registration and authentication with JWT  
✅ Secure password encryption with BCrypt  
✅ Task CRUD operations  
✅ Task assignment to users (auto-assigned to current user)  
✅ Task status management (PENDING, IN_PROGRESS, COMPLETED)  
✅ Deadline tracking with LocalDateTime  
✅ Email notifications for task assignments  
✅ Scheduled reminders for upcoming deadlines  
✅ H2 console for database inspection  
✅ RESTful API design  
✅ Stateless JWT authentication  

---

## Project Structure

```
backend/
├── src/main/java/com/trackmyteam/backend/
│   ├── config/              # Security and application configuration
│   │   ├── ApplicationConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── SecurityConfig.java
│   ├── controller/          # REST API controllers
│   │   ├── AuthController.java
│   │   └── TaskController.java
│   ├── model/              # JPA entities and enums
│   │   ├── User.java
│   │   ├── Task.java
│   │   ├── Role.java (USER, ADMIN)
│   │   └── TaskStatus.java (PENDING, IN_PROGRESS, COMPLETED)
│   ├── repository/         # Data access layer
│   │   ├── UserRepository.java
│   │   └── TaskRepository.java
│   ├── service/            # Business logic
│   │   ├── AuthService.java
│   │   ├── TaskService.java
│   │   ├── EmailService.java
│   │   └── ReminderScheduler.java
│   └── util/               # Utility classes
│       └── JwtUtil.java
└── src/main/resources/
    └── application.properties  # Application configuration
```

---

## Development Notes

- The application uses **Lombok** to reduce boilerplate code. Make sure your IDE has Lombok plugin installed.
- **H2 Console** is enabled for easy database inspection during development.
- JWT tokens expire after **24 hours** by default (86400000 milliseconds).
- The reminder scheduler runs daily to check for tasks with upcoming deadlines.
- Tasks are automatically assigned to the currently authenticated user.
- Passwords are encrypted using BCrypt before storing in the database.

---

## Common Issues and Solutions

### Issue: "Access Denied" when accessing endpoints
**Solution:** Make sure you're including the JWT token in the Authorization header:
```
Authorization: Bearer <your-token>
```

### Issue: Token expired
**Solution:** Login again to get a new token. Tokens expire after 24 hours.

### Issue: Cannot connect to H2 Console
**Solution:** 
1. Make sure the application is running
2. Go to `http://localhost:8080/h2-console`
3. Use JDBC URL: `jdbc:h2:mem:trackmyteam`
4. Username: `sa`, Password: (leave empty)

---

## License

This project is for educational and demonstration purposes.
