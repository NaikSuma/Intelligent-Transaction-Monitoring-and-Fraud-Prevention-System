# Intelligent-Transaction-Monitoring-and-Fraud-Prevention-System
Spring Boot-based fraud detection system with JWT authentication, RBAC, transaction monitoring, risk scoring, and automated fraud alerts.
 
## Features 

- User registration and login
- BCrypt password encryption
- JWT-based authentication
- Role-Based Access Control (ADMIN, ANALYST, CUSTOMER)
- Bank account management
- Transaction processing
- Rule-based fraud detection
- Risk score calculation
- High transaction amount detection
- Historical transaction amount comparison
- Rapid transaction detection
- High transaction frequency detection
- New IP detection
- New device detection
- Same IP/device detection across accounts
- Automatic account blocking
- Fraud alert generation
- Admin account unblocking
  

## Technology Stack

- Java
- Spring Boot
- Spring Security
- JWT
- BCrypt
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- REST APIs
- Postman

## How It Works

1. Users register with a specific role.
2. Users authenticate using username and password.
3. Passwords are securely stored using BCrypt.
4. Upon successful login, a JWT token is generated.
5. Protected APIs require JWT authentication.
6. ADMIN users can create and manage bank accounts.
7. Customers perform transactions through REST APIs.
8. Each transaction is evaluated by the fraud detection engine.
9. Multiple risk indicators contribute to the overall risk score.
10. High-risk transactions generate fraud alerts.
11. Accounts exceeding the configured risk threshold are automatically blocked.
12. ADMIN users can unblock blocked accounts.
