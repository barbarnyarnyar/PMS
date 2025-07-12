# Authentication & Security Guide

## Overview
Your Hotel PMS now includes comprehensive user authentication and security features using JWT tokens and role-based access control.

## Default Users
After starting the application, these default users are created:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ROLE_ADMIN | admin@tolimoli.com |
| manager | manager123 | ROLE_MANAGER | manager@tolimoli.com |
| receptionist | reception123 | ROLE_RECEPTIONIST | reception@tolimoli.com |

## Authentication Endpoints

### POST /api/auth/signin
Login to get JWT token
```json
{
  "usernameOrEmail": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@tolimoli.com",
  "fullName": "System Administrator",
  "roles": ["ROLE_ADMIN"],
  "permissions": ["USER_MANAGEMENT", "ROOM_MANAGEMENT", ...]
}
```

### POST /api/auth/signup
Register new user
```json
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "123-456-7890",
  "department": "Reception",
  "role": ["ROLE_USER"]
}
```

### GET /api/auth/me
Get current user info (requires Bearer token)

### POST /api/auth/signout
Logout user

### POST /api/auth/refresh
Refresh JWT token

## Using JWT Tokens

Include JWT token in Authorization header:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Roles & Permissions

### ROLE_ADMIN
- Full system access
- All permissions

### ROLE_MANAGER
- Hotel management operations
- Room, reservation, payment management
- User viewing access

### ROLE_RECEPTIONIST
- Front desk operations
- Check-in/check-out
- Guest management
- Payment processing

### ROLE_USER
- Basic read-only access
- View rooms, reservations, guests

## Protected Endpoints

### User Management (`/api/users/**`)
- Requires: `USER_MANAGEMENT` permission
- Admin only access

### Hotel Operations
- **Rooms**: `ROOM_VIEW` or `ROOM_MANAGEMENT`
- **Reservations**: `RESERVATION_VIEW` or `RESERVATION_MANAGEMENT`
- **Payments**: `PAYMENT_VIEW` or `PAYMENT_MANAGEMENT`
- **Guests**: `GUEST_VIEW` or `GUEST_MANAGEMENT`
- **Channels**: `CHANNEL_VIEW` or `CHANNEL_MANAGEMENT`
- **Rates**: `RATE_VIEW` or `RATE_MANAGEMENT`
- **Folio**: `FOLIO_VIEW` or `FOLIO_MANAGEMENT`

## API Testing Examples

### Login
```bash
curl -X POST http://localhost:8081/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"admin123"}'
```

### Access Protected Endpoint
```bash
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Create Room (with auth)
```bash
curl -X POST http://localhost:8081/api/rooms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roomNumber":"101","roomType":"SINGLE","capacity":1,"baseRate":100}'
```

## Security Features

1. **JWT Authentication**: Stateless token-based authentication
2. **Role-Based Access Control**: Hierarchical permission system
3. **Password Encryption**: BCrypt hashing
4. **Method-Level Security**: `@PreAuthorize` annotations
5. **CORS Support**: Cross-origin request handling
6. **Session Management**: Stateless configuration

## Configuration

JWT settings in `application.yml`:
```yaml
app:
  jwtSecret: mySecretKey
  jwtExpirationMs: 86400000  # 24 hours
```

## Starting the Application

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments="--server.port=8081"
```

The application will automatically create the database schema and initialize default users, roles, and permissions.