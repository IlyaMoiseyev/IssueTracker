# 📋 Issue Tracker

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue.svg)](https://www.postgresql.org/)

A multifunctional task and bug management system that ensures the creation,
tracking and control of the workflow in development teams.

## 🛠 Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.2.5
* **Security:** Spring Security, JWT, BCrypt
* **Database:** PostgreSQL 15
* **Migrations:** Flyway
* **ORM:** Spring Data JPA, Hibernate
* **Logging:** SLF4J + Lombok
* **Documentation:** Swagger UI / OpenAPI 3.0
* **Build:** Maven

## ✨ Features

- 🎯 **Task Management** — create, edit, delete tasks with statuses and priorities, tags
- 👥 **Role model** — `ADMIN`, `DEVELOPER`, `REPORTER` with access control
- 🔐 **JWT authentication** — stateless sessions, BCrypt password hashing
- 📝 **Comments** — discussion of tasks with reference to the author
- 🏷 **Tag system** — task categorization (BACKEND, FRONTEND, FEATURE, BUG)
- 📋 **Change History** — complete audit of all changes to task fields
- 🛡 **Access control** — users only see their own or assigned tasks.
- ⚙ **DTO Validation** — validation of input data via Bean Validation
- 🗄 **Database migrations** — schema versioning via Flyway

## 🚀 Quick Start

### 📦 Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL

### 🖥️ Run Locally

1. **Clone the repository:**

```bash
git clone https://github.com/IlyaMoiseyev/IssueTracker
cd IssueTracker
```

2. **Configure application.properties for your database:**

```bash
# spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
# spring.datasource.username=your_username
# spring.datasource.password=your_db_password
```

3. **Build the project:**

```bash
./mvnw clean package -DskipTests
```

4. **Run the App:**

```bash
mvn spring-boot:run
```

### 🔑 Authentication Flow

1. **Register** a new user at `/auth/register`.
2. **Login** at `/auth/login` to receive your **Access** and **Refresh** tokens.
3. Use the Access Token in the `Authorization` header as `Bearer <token>` for all protected endpoints.

## 🗄 DB structure

### Tables:

- `users` — users with roles
- `issues` — tasks (title, description, status, priority, reporter, assignee)
- `comments` — comments on tasks
- `issue_history` — issue field change history
- `issue_statuses` — directory of statuses (OPEN, IN_PROGRESS, CLOSED)
- `issue_priorities ` — directory of priorities (LOW, MEDIUM, HIGH)
- `roles` — directory of roles (ADMIN, DEVELOPER, REPORTER)
- `tags` — tag reference (BACKEND, FRONTEND, FEATURE, BUG)
- `issue_tags` — the relationship of tasks and tags (many-to-many)

## 📖 API endpoints

### 🔐 Authentication (/auth)

| Method | Path             | Access | Description                   |
|--------|------------------|--------|-------------------------------|
| `POST` | `/auth/register` | All    | Registering a new user        |
| `POST` | `/auth/login`    | All    | Login and receive a JWT token |

### 👤 Users (/user)

| Method   | Path            | Access     | Description         |
|----------|-----------------|------------|---------------------|
| `GET`    | `/user`         | ADMIN      | List of all users   |
| `GET`    | `/user/{id}`    | ADMIN      | User Information    |
| `POST`   | `/user`         | ADMIN      | Create new user     |
| `PUT`    | `/user`         | ADMIN      | Update user         |
| `PUT`    | `/user/current` | Authorized | Update your profile |
| `DELETE` | `/auth/login`   | ADMIN      | Delete user         |

### 🎫 Issue (/issue)

| Method   | Path          | Access     | Description          |
|----------|---------------|------------|----------------------|
| `GET`    | `/issue`      | Authorized | Current user's tasks |
| `GET`    | `/issue/{id}` | By rights  | Task by ID           |
| `POST`   | `/issue`      | Authorized | Create new issue     |
| `PUT`    | `/user`       | By rights  | Update issue         |
| `DELETE` | `/issue/{id}` | By rights  | Delete issue         |

### 💬 Comments (/comment)

| Method   | Path                       | Access       | Description                |
|----------|----------------------------|--------------|----------------------------|
| `GET`    | `/comment/{id}`            | By rights    | Get a comment by ID        |
| `GET`    | `/comment`                 | ADMIN        | Get all comments           |
| `GET`    | `/comment/issue/{issueId}` | By rights    | Get all comments for issue |
| `POST`   | `/comment`                 | Authorized   | Add a comment to an issue  |
| `PUT`    | `/comment`                 | Author/ADMIN | Edit a comment             |
| `DELETE` | `/comment/{id}`            | Author/ADMIN | Delete comment             |

### 📋 History (/history)

| Method | Path                       | Access    | Description             |
|--------|----------------------------|-----------|-------------------------|
| `GET`  | `/history`                 | ADMIN     | All history of changes  |
| `GET`  | `/history/{id}`            | By rights | Recording history by ID |
| `GET`  | `/history/issue/{issueId}` | By rights | Issue change history    |

### 🏷 IssueTag (/issues)

| Method   | Path                              | Access     | Description                   |
|----------|-----------------------------------|------------|-------------------------------|
| `GET`    | `/issues/{issueId}/tag`           | By rights  | Task tags                     |
| `GET`    | `/issues/search/tag/{tagName}`    | Authorized | Search for issues by tag      |
| `POST`   | `/issues/{issueId}/tag/{tagName}` | By rights  | Add a tag to an issue         |
| `DELETE` | `/issues/{issueId}/tag/{tagName}` | By rights  | Remove the tag from the issue |

### 🎭 Role (/role)

| Method | Path           | Access | Description   |
|--------|----------------|--------|---------------|
| `GET`  | `/role`        | ADMIN  | All role list |
| `GET`  | `/role/{id}`   | ADMIN  | Role by ID    |
| `GET`  | `/role/{name}` | ADMIN  | Role by name  |

### 🏷️ Tag (/tag)

| Method | Path          | Access | Description  |
|--------|---------------|--------|--------------|
| `GET`  | `/tag`        | ADMIN  | All tag list |
| `GET`  | `/tag/{id}`   | ADMIN  | Tag by ID    |
| `GET`  | `/tag/{name}` | ADMIN  | Tag by name  |

### 📊 Status (/status)

| Method | Path             | Access | Description       |
|--------|------------------|--------|-------------------|
| `GET`  | `/status`        | ADMIN  | All statuses list |
| `GET`  | `/status/{id}`   | ADMIN  | Status by ID      |
| `GET`  | `/status/{name}` | ADMIN  | Status by name    |

### ❗ Priority (/priority)

| Method | Path               | Access | Description         |
|--------|--------------------|--------|---------------------|
| `GET`  | `/priority`        | ADMIN  | All priorities list |
| `GET`  | `/priority/{id}`   | ADMIN  | Priority by ID      |
| `GET`  | `/priority/{name}` | ADMIN  | Priority by name    |

### 🔐 Access rights

| Action            | ADMIN | REPORTER (own) | DEVELOPER (own / assigned) | Other |
|-------------------|-------|----------------|----------------------------|-------|
| Viewing issues    | ✅ All | ✅ Your own     | Your own + assigned        | ❌     |
| Creating issue    | ✅     | ✅              | ✅                          | ❌     |
| Changing the task | ✅     | ✅ Your own     | Your own + assigned        | ❌     |
| Deleting an issue | ✅     | ❌              | ❌                          | ❌     |
| Viewing tags      | ✅     | ✅ Your own     | Your own + assigned        | ❌     |
| Tag management    | ✅     | ✅ Your own     | Your own                   | ❌     |

## 📂 Project Structure

```text
src/main/java/com/moiseyev/issuetracker/
├── config/              # Security, JWT, and Spring configurations
├── controller/          # REST Controllers (API endpoints)
├── exception/           # Custom exceptions and Global Exception Handler
├── model/               # Data models and structures
│   ├── dto/             # Request/Response objects
│   ├── entity/          # JPA Entities (Database tables)
│   ├── enums/           # Shared enumerations (RoleType, StatusType, PriorityLevel, TagType)
│   └── mapper/          # Mappers Entity -> DTO
├── repository/          # Spring Data JPA Repositories
├── security/            # JWT filter, service, CustomUserDetails
└── service/             # Business logic (Services)

src/main/resources/
├── db/migration/          # Flyway SQL migration scripts
└── application.properties # Main configuration file
```

## 📬 Contact & Support

If you have any questions about this project or want to discuss the implementation details, feel free to reach out:

* **GitHub:** [@moiseyevIlya](https://github.com/IlyaMoiseyev)
* **Email:** moiseyev.ilya@gmail.com