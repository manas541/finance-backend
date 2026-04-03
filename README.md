# Finance Dashboard Backend

A complete REST API backend for managing financial records with role-based access control. Built with Spring Boot, PostgreSQL, and Docker.

**🔗 Live API:** https://finance-backend-1-vmei.onrender.com/api/v1

## 📋 Quick Links

- **Hosted URL:** https://finance-backend-1-vmei.onrender.com
- **API Base URL:** https://finance-backend-1-vmei.onrender.com/api/v1
- **GitHub Repository:** https://github.com/manas541/finance-backend
- **Local URL:** http://localhost:8080/api/v1

## ✨ Features

### User Management
- Create, update, delete users
- Three role types: VIEWER, ANALYST, ADMIN
- User activation/deactivation
- Soft delete (data preservation)
- Role-based permissions

### Financial Records Management
- Create income/expense transactions
- View, update, delete records
- Filter by date, category, type
- Input validation
- Soft delete support

### Role-Based Access Control

| Feature | VIEWER | ANALYST | ADMIN |
|---------|--------|---------|-------|
| View Dashboard | ✅ | ✅ | ✅ |
| View Own Records | ✅ | ✅ | ✅ |
| Create Records | ❌ | ✅ | ✅ |
| Update Own Records | ❌ | ✅ | ✅ |
| Delete Records | ❌ | ❌ | ✅ |
| View Any Records | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |
| Manage Roles | ❌ | ❌ | ✅ |

### Dashboard Analytics
- Total income calculation
- Total expenses calculation
- Net balance (Income - Expenses)
- Category-wise breakdown
- Transaction counts

## 🛠️ Technology Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.1.5
- **ORM:** JPA/Hibernate
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Utilities:** Lombok
- **Deployment:** Docker + Render
- **API Type:** RESTful


### Key Components

- **25 Java Classes** - Well-organized, fully commented
- **22 REST API Endpoints** - CRUD operations for all entities
- **3 Database Tables** - roles, users, financial_records
- **4 DTO Classes** - Type-safe API contracts (passwords excluded)
- **Access Control** - Centralized permission logic
- **Error Handling** - Meaningful error responses
- **Docker Support** - Production-ready containerization

## 🚀 Getting Started

### Prerequisites

- Java 17+
- PostgreSQL (local development)
- Maven 3.6+
- Docker (optional, for containerized deployment)

### Local Setup (Without Docker)

#### 1. Create Database
```bash
psql -U postgres
CREATE DATABASE finance_db;
\q
```

#### 2. Clone Repository
```bash
git clone https://github.com/manas541/finance-backend.git
cd finance-backend
```

#### 3. Update Configuration

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=your_postgres_password
```

#### 4. Run Application
```bash
mvn clean spring-boot:run
```

**Server starts at:** http://localhost:8080/api/v1

### Local Setup (With Docker)

#### 1. Start Services
```bash
docker-compose up
```

**Both PostgreSQL and Backend start automatically**

#### 2. Access API

http://localhost:8080/api/v1

#### 3. Stop Services
```bash
docker-compose down
```

## 📡 API Endpoints

### Base URL
- **Local:** http://localhost:8080/api/v1
- **Hosted:** https://finance-backend-1-vmei.onrender.com/api/v1

### Roles (6 endpoints)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/roles` | Get all roles | ALL |
| POST | `/roles` | Create role | ADMIN |
| GET | `/roles/{id}` | Get role by ID | ALL |
| PUT | `/roles/{id}` | Update role | ADMIN |
| DELETE | `/roles/{id}` | Delete role | ADMIN |

### Users (9 endpoints)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/users` | Get all users | ALL |
| POST | `/users` | Create user | ADMIN |
| GET | `/users/{id}` | Get user by ID | ALL |
| GET | `/users/email/{email}` | Get by email | ALL |
| GET | `/users/active/all` | Get active users | ALL |
| PUT | `/users/{id}` | Update user | ADMIN |
| DELETE | `/users/{id}` | Delete user | ADMIN |
| POST | `/users/{id}/activate` | Activate user | ADMIN |
| POST | `/users/{id}/deactivate` | Deactivate user | ADMIN |

### Financial Records (7 endpoints)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/records?userId=1` | Get my records | VIEWER, ANALYST, ADMIN |
| POST | `/records?userId=1` | Create record | ANALYST, ADMIN |
| GET | `/records/{id}?userId=1` | Get record by ID | Owner, ADMIN |
| PUT | `/records/{id}?userId=1` | Update record | ANALYST, ADMIN |
| DELETE | `/records/{id}?userId=1` | Delete record | ADMIN |
| GET | `/records/dashboard/summary?userId=1` | Dashboard data | ALL |
| GET | `/records/user/{userId}?requestingUserId=1` | Get user records | ADMIN |

## 📝 Example API Requests

### 1. Create a Role
```bash
curl -X POST https://finance-backend-1-vmei.onrender.com/api/v1/roles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "VIEWER",
    "description": "Can only view dashboard",
    "roleType": "VIEWER"
  }'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "name": "VIEWER",
    "description": "Can only view dashboard",
    "roleType": "VIEWER"
}
```

### 2. Create a User
```bash
curl -X POST https://finance-backend-1-vmei.onrender.com/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": {"id": 1}
  }'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": {
        "id": 1,
        "name": "VIEWER",
        "description": "Can only view dashboard",
        "roleType": "VIEWER"
    },
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

### 3. Create Financial Record
```bash
curl -X POST "https://finance-backend-1-vmei.onrender.com/api/v1/records?userId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "SALARY",
    "transactionDate": "2024-01-15",
    "notes": "Monthly salary"
  }'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "amount": 5000.00,
    "type": "INCOME",
    "category": "SALARY",
    "transactionDate": "2024-01-15",
    "notes": "Monthly salary",
    "createdByUserId": 1,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

### 4. Get Dashboard Summary
```bash
curl -X GET "https://finance-backend-1-vmei.onrender.com/api/v1/records/dashboard/summary?userId=1"
```

**Response (200 OK):**