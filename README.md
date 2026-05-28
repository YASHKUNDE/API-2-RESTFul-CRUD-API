# API-2-RESTFul-CRUD-API

This is a fully responsive REST CRUD API built with Java Spring Boot for backend development, completed in 2025.

# 🚀 Java Spring Boot — Fully Responsive REST CRUD API

> **This is a Java Spring Boot API — Fully Responsive REST CRUD API — crafted for a better experience in building a backend API, complete in 2025.**

A production-ready, fully structured RESTful CRUD API built with **Spring Boot 4.0.3** and **Java 17**. This project covers user registration, secure login, and full user management with clean structured JSON responses, BCrypt password hashing, input validation, duplicate detection, and CORS support — everything you need to kickstart a solid backend.

---

## 📁 Project Structure

```
API-2/
├── src/
│   └── main/
│       ├── java/in/api_2/
│       │   ├── Api2Application.java          # Spring Boot Entry Point
│       │   ├── Login_API.java                # JPA Entity (User Model)
│       │   ├── Login_API_Controller.java     # REST Controller (All Endpoints)
│       │   ├── Login_API_Repository.java     # JPA Repository (DB Queries)
│       │   ├── Login_Configuration.java      # CORS Configuration
│       │   ├── Login_Request.java            # Request DTO (Login/Search)
│       │   └── Login_Response.java           # Unified API Response Wrapper
│       └── resources/
│           └── application.properties        # DB & JPA Configuration
├── pom.xml                                   # Maven Dependencies
└── mvnw / mvnw.cmd                           # Maven Wrapper
```

---

## ⚙️ Tech Stack

| Technology              | Version / Detail              |
|-------------------------|-------------------------------|
| Java                    | 17                            |
| Spring Boot             | 4.0.3                         |
| Spring Data JPA         | Hibernate ORM                 |
| Spring Security         | BCrypt Password Encoding       |
| Spring Web MVC          | REST API                      |
| Spring Boot Actuator    | Health & Monitoring           |
| Spring Boot Validation  | Bean Validation (`@Valid`)    |
| Database                | PostgreSQL                    |
| Build Tool              | Maven                         |
| Lombok                  | Boilerplate Reduction         |
| Dev Tools               | Spring Boot DevTools          |

---

## 🗄️ Database Configuration

Edit `src/main/resources/application.properties` to match your PostgreSQL setup:

```properties
spring.application.name=API-2

# Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/Registration
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> ✅ Make sure a PostgreSQL database named **`Registration`** exists before running.

---

## 📦 User Entity (Model)

| Field      | Type    | Constraint     |
|------------|---------|----------------|
| `id`       | Integer | Auto-generated (Primary Key) |
| `name`     | String  | Required       |
| `userName` | String  | Unique         |
| `email`    | String  | Unique         |
| `mobile`   | String  | Unique         |
| `pass`     | String  | BCrypt Hashed  |

---

## 🌐 API Endpoints

Base URL: `http://localhost:8080/api`

---

### 1. 📋 View All Users
**`GET`** `/api/view_users`

**Response (200 — Success):**
```json
{
  "status": "200",
  "message": "Users found successfully",
  "users": [ { "id": 1, "name": "John", "userName": "john01", "email": "john@mail.com", "mobile": "9999999999", "pass": "$2a$..." } ]
}
```

**Response (404 — No Users):**
```json
{ "status": "404", "message": "No users found", "users": null }
```

---

### 2. ➕ Add User
**`POST`** `/api/add_users`

**Request Body:**
```json
{
  "name": "John Doe",
  "userName": "johndoe",
  "email": "john@example.com",
  "mobile": "9876543210",
  "pass": "mypassword"
}
```

**Response (201 — Created):**
```json
{ "status": "201", "message": "User saved successfully", "users": [...] }
```

**Response (400 — Duplicate):**
```json
{ "status": "400", "message": "User already exists. Username and Email already exist.", "users": null }
```

> 🔒 Password is automatically **BCrypt hashed** before saving.

---

### 3. ✏️ Update User
**`PUT`** `/api/update_users`

**Request Body** *(must include `id`)***:**
```json
{
  "id": 1,
  "name": "John Updated",
  "userName": "johnnew",
  "email": "new@example.com",
  "mobile": "9876543210",
  "pass": "newpassword"
}
```

**Response (200 — Updated):**
```json
{ "status": "200", "message": "User updated successfully", "users": [...] }
```

**Response (404 — Not Found):**
```json
{ "status": "404", "message": "User with ID 1 not found.", "users": null }
```

> 🔒 Password is re-hashed only if it has been changed.

---

### 4. 🗑️ Delete User
**`DELETE`** `/api/delete_users`

**Request Body:**
```json
{ "id": 1 }
```

**Response (200 — Deleted):**
```json
{ "status": "200", "message": "User deleted successfully", "users": [...] }
```

**Response (404 — Not Found):**
```json
{ "status": "404", "message": "User with ID 1 not found.", "users": null }
```

---

### 5. 🔑 Login (Email or Username)
**`POST`** `/api/login`

**Request Body (via Email):**
```json
{ "email": "john@example.com", "pass": "mypassword" }
```

**Request Body (via Username):**
```json
{ "userName": "johndoe", "pass": "mypassword" }
```

**Response (200 — Login Successful):**
```json
{ "status": "200", "message": "Login successful via email", "users": [...] }
```

**Response (400 — Invalid Credentials):**
```json
{ "status": "400", "message": "Invalid password", "users": null }
```

> ✅ Supports login via **email** or **username**. Password verified using BCrypt `matches()`.

---

### 6. 📱 Find User by Mobile Number
**`POST`** `/api/mobileid`

**Request Body:**
```json
{ "id": "9876543210" }
```

**Response (200 — Found):**
```json
{ "status": "200", "message": "User found", "users": [...] }
```

**Response (400 — Not Found):**
```json
{ "status": "400", "message": "No user found with this mobile number", "users": null }
```

---

## 🔄 Unified API Response Format

All endpoints return a consistent response structure:

```json
{
  "status": "200 | 201 | 400 | 404 | 500",
  "message": "Descriptive message",
  "users": [ ...user objects... ] 
}
```

---

## 🌍 CORS Configuration

CORS is pre-configured in `Login_Configuration.java` for the following origins:

| Origin                          | Purpose                     |
|---------------------------------|-----------------------------|
| `http://localhost:5173`         | Vite / React Dev Server     |
| `http://localhost:5500`         | VS Code Live Server         |
| `http://127.0.0.1:5500`        | VS Code Live Server (Alt)   |

**Allowed Methods:** `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`

---

## 🔐 Security Features

- **BCrypt Password Hashing** — Passwords are never stored in plain text.
- **Duplicate Detection** — Checks for existing `userName`, `email`, and `mobile` before insert/update.
- **Input Validation** — `@Valid` + `BindingResult` for field-level error messages.
- **Password Re-hashing Guard** — On update, password is only re-hashed if it has actually changed.

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+ (or use included `mvnw`)
- PostgreSQL running locally

### Steps

```bash
# 1. Clone the project
git clone <your-repo-url>
cd API-2

# 2. Create PostgreSQL database
psql -U postgres -c "CREATE DATABASE Registration;"

# 3. Update credentials in application.properties

# 4. Run the application
./mvnw spring-boot:run
# OR on Windows
mvnw.cmd spring-boot:run
```

The API will be available at: **`http://localhost:8080`**

You will see in the console:
```
Spring Boot Running Successfully...!
```

---

## 🧪 Testing with Postman

Import the following base URL into Postman:
```
http://localhost:8080/api
```

Recommended collection order:
1. `POST /add_users` — Register a user
2. `GET /view_users` — Confirm registration
3. `POST /login` — Login with email or username
4. `PUT /update_users` — Update user details
5. `POST /mobileid` — Lookup by mobile
6. `DELETE /delete_users` — Remove user

---

## 📌 Notes

- The `target/` folder and compiled `.class` files are included in the ZIP but should be excluded via `.gitignore` in version-controlled repositories.
- Spring Boot DevTools is enabled for **hot reload** during development.
- Spring Boot Actuator is included for health check at `/actuator/health`.

---

## 👨‍💻 Author

Built with ❤️ using **Spring Boot 4.0.3 + Java 17 + PostgreSQL** — 2025