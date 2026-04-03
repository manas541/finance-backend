# Finance Dashboard Backend

Spring Boot REST API for managing financial records with role-based access control.

## Features

- **User Management**: Create, update, delete users
- **Role Management**: VIEWER, ANALYST, ADMIN roles
- **Financial Records**: Create income/expense transactions
- **Access Control**: Role-based permissions
- **Dashboard**: Income, expenses, balance summaries
- **PostgreSQL**: Persistent data storage

## Technology Stack

- **Java 17**
- **Spring Boot 4.0.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**

## Prerequisites

- Java 17+
- PostgreSQL
- Maven 3.6+

## Setup

### 1. Create Database
```bash
psql -U postgres
CREATE DATABASE finance_db;
\q
```

### 2. Update Configuration

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=your_password
```

### 3. Run Application
```bash
mvn clean spring-boot:run
```

Server starts at: `http://localhost:8080/api/v1`

## API Endpoints

### Roles (6 endpoints)
- `GET /roles` - Get all roles
- `POST /roles` - Create role
- `GET /roles/{id}` - Get role by ID
- `PUT /roles/{id}` - Update role
- `DELETE /roles/{id}` - Delete role

### Users (9 endpoints)
- `GET /users` - Get all users
- `POST /users` - Create user
- `GET /users/{id}` - Get user by ID
- `GET /users/email/{email}` - Get by email
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user
- `POST /users/{id}/activate` - Activate
- `POST /users/{id}/deactivate` - Deactivate
- `GET /users/active/all` - Get active users

### Financial Records (7 endpoints)
- `GET /records?userId=1` - Get my records
- `POST /records?userId=1` - Create record
- `GET /records/{id}?userId=1` - Get record by ID
- `PUT /records/{id}?userId=1` - Update record
- `DELETE /records/{id}?userId=1` - Delete record
- `GET /records/dashboard/summary?userId=1` - Dashboard data

## Example Requests

### Create Role
```bash
curl -X POST http://localhost:8080/api/v1/roles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "VIEWER",
    "description": "Can view dashboard",
    "roleType": "VIEWER"
  }'
```

### Create User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": {"id": 1}
  }'
```

### Create Financial Record
```bash
curl -X POST "http://localhost:8080/api/v1/records?userId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "SALARY",
    "transactionDate": "2024-01-15",
    "notes": "Monthly salary"
  }'
```

## Role Permissions

### VIEWER
- ✅ View dashboard
- ✅ View records
- ❌ Create records
- ❌ Update records
- ❌ Delete records

### ANALYST
- ✅ View dashboard
- ✅ View own records
- ✅ Create records
- ✅ Update own records
- ❌ Delete records

### ADMIN
- ✅ Full access
- ✅ View all records
- ✅ Create records
- ✅ Update any record
- ✅ Delete records

## Testing

Use Postman or Thunder Client:

1. Create roles (VIEWER, ANALYST, ADMIN)
2. Create users with different roles
3. Create financial records
4. Test access control (VIEWER cannot create)

## Database Schema

### roles
- id, name, description, role_type

### users
- id, name, email, password, role_id, is_active, is_deleted, created_at, updated_at

### financial_records
- id, created_by_user_id, amount, type, category, transaction_date, notes, is_deleted, created_at, updated_at

## Future Enhancements

- JWT Authentication
- Pagination
- Unit tests
- Swagger documentation
- Email notifications
- Advanced filtering


## Author

Manas Purohit