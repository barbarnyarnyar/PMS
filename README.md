# PMS
Property Management System
a# Tolimoli Hotel PMS 🏨

A comprehensive Hotel Property Management System built with Spring Boot and MySQL, featuring booking management, channel distribution, and financial tracking.

## 🎯 **Overview**

Tolimoli PMS is an all-in-one hotel management solution that covers the 6 essential features every hotel needs:

1. **🏨 Room Management** - Inventory, status, and availability tracking
2. **👤 Guest Management** - Guest profiles, history, and preferences  
3. **📋 Reservations** - Booking processing and management
4. **💰 Billing & Payments** - Financial transactions and folio management
5. **📊 Basic Reporting** - Occupancy, revenue, and guest analytics
6. **🌐 Channel Connectivity** - OTA integration and rate distribution

## 🏗️ **Architecture**

- **Backend**: Spring Boot 3.2.1 with Java 17
- **Database**: MySQL 8.0+ 
- **Frontend**: Angular 17+ (planned)
- **Deployment**: Docker containerization
- **Architecture Pattern**: Monolithic (with microservices migration path)

## 🚀 **Quick Start**

### **Prerequisites**
- Docker Desktop installed
- MySQL 8.0+ running locally
- Git

### **1. Clone Repository**
```bash
git clone https://github.com/your-username/tolimoli-pms.git
cd tolimoli-pms
```

### **2. Setup MySQL Database**
```sql
# Connect to MySQL
mysql -u root -p

# Create database and user
CREATE DATABASE IF NOT EXISTS pms_db;
CREATE USER 'pms_user'@'%' IDENTIFIED BY 'pms_password';
GRANT ALL PRIVILEGES ON pms_db.* TO 'pms_user'@'%';
FLUSH PRIVILEGES;
EXIT;
```

### **3. Configure Environment**
```bash
# Set database credentials
export DB_USERNAME=pms_user
export DB_PASSWORD=pms_password
```

### **4. Run Application**
```bash
# Build and start with Docker
docker-compose up --build

# Or run locally (requires Maven)
mvn spring-boot:run
```

### **5. Verify Installation**
```bash
# Test application health
curl http://localhost:8080/actuator/health

# Test welcome endpoint
curl http://localhost:8080
```

## 📋 **Project Structure**

```
tolimoli-pms/
├── README.md
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/tolimoli/pms/
│   │   │   ├── PmsApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── RoomController.java
│   │   │   │   ├── GuestController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   └── ReportController.java
│   │   │   ├── service/
│   │   │   │   ├── RoomService.java
│   │   │   │   ├── GuestService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── FolioChargeService.java
│   │   │   │   ├── ChannelService.java
│   │   │   │   └── RateService.java
│   │   │   ├── repository/
│   │   │   │   ├── RoomRepository.java
│   │   │   │   ├── GuestRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── FolioChargeRepository.java
│   │   │   │   ├── ChannelRepository.java
│   │   │   │   └── RateRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── Room.java
│   │   │   │   ├── Guest.java
│   │   │   │   ├── Reservation.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── FolioCharge.java
│   │   │   │   ├── Channel.java
│   │   │   │   └── Rate.java
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   └── exception/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   └── test/
└── docs/
```

## 🗄️ **Database Schema**

### **Core Entities**

```sql
-- Rooms table
CREATE TABLE rooms (
    room_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(50) UNIQUE NOT NULL,
    room_type ENUM('SINGLE', 'DOUBLE', 'DELUXE', 'SUITE'),
    capacity INT,
    base_rate DECIMAL(10,2),
    status ENUM('AVAILABLE', 'OCCUPIED', 'DIRTY', 'MAINTENANCE'),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Guests table
CREATE TABLE guests (
    guest_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    id_number VARCHAR(50),
    id_type ENUM('PASSPORT', 'DRIVER_LICENSE', 'NATIONAL_ID'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Additional tables: reservations, payments, folio_charges, channels, rates
```

## 🔧 **Configuration**

### **Application Properties**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pms_db
    username: ${DB_USERNAME:pms_user}
    password: ${DB_PASSWORD:pms_password}
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### **Environment Variables**
```bash
# Database
DB_USERNAME=pms_user
DB_PASSWORD=pms_password

# Application
SPRING_PROFILES_ACTIVE=dev

# External Services (optional)
STRIPE_SECRET_KEY=sk_test_...
SENDGRID_API_KEY=SG...
```

## 🚀 **Deployment**

### **Docker Deployment**
```bash
# Production build
docker build -t tolimoli/pms:latest .

# Run container
docker run -d --name pms-app \
  -p 8080:8080 \
  -e DB_USERNAME=pms_user \
  -e DB_PASSWORD=pms_password \
  tolimoli/pms:latest
```

### **Docker Compose**
```bash
# Development
docker-compose up --build

# Production
docker-compose -f docker-compose.prod.yml up -d
```

## 📝 **API Documentation**

### **Core Endpoints**

#### **Rooms**
```
GET    /api/rooms              # Get all rooms
GET    /api/rooms/{id}         # Get room by ID
POST   /api/rooms              # Create new room
PUT    /api/rooms/{id}         # Update room
DELETE /api/rooms/{id}         # Delete room
GET    /api/rooms/available    # Get available rooms
```

#### **Reservations**
```
GET    /api/reservations                    # Get all reservations
GET    /api/reservations/{id}               # Get reservation by ID
POST   /api/reservations                    # Create new reservation
PUT    /api/reservations/{id}               # Update reservation
PUT    /api/reservations/{id}/checkin       # Check-in guest
PUT    /api/reservations/{id}/checkout      # Check-out guest
DELETE /api/reservations/{id}               # Cancel reservation
```

#### **Guests**
```
GET    /api/guests              # Get all guests
GET    /api/guests/{id}         # Get guest by ID
POST   /api/guests              # Create new guest
PUT    /api/guests/{id}         # Update guest
GET    /api/guests/{id}/history # Get guest booking history
```

### **Swagger Documentation**
Access interactive API documentation at: `http://localhost:8080/swagger-ui.html`

## 🧪 **Testing**

### **Run Tests**
```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# With coverage
mvn test jacoco:report
```

### **Test Coverage**
- **Target**: 80%+ code coverage
- **Reports**: `target/site/jacoco/index.html`

## 🔧 **Development**

### **Local Development Setup**
```bash
# Install dependencies
mvn dependency:resolve

# Run in development mode
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Hot reload (requires spring-boot-devtools)
mvn spring-boot:run
```

### **Database Migrations**
```bash
# Generate migration
mvn flyway:migrate

# Validate migrations
mvn flyway:validate

# Clean database (development only)
mvn flyway:clean
```

## 🎯 **Features**

### **✅ Implemented**
- [x] Basic Spring Boot application setup
- [x] MySQL database connection
- [x] Docker containerization
- [x] Health check endpoints
- [x] Entity models (Room, Guest, Reservation, etc.)
- [x] Repository layer with custom queries
- [x] Service layer with business logic

### **🚧 In Progress**
- [ ] REST API controllers
- [ ] Authentication & authorization
- [ ] Payment integration (Stripe)
- [ ] Email notifications
- [ ] Channel manager integration

### **📋 Planned**
- [ ] Angular frontend
- [ ] Advanced reporting
- [ ] Multi-property support
- [ ] Mobile app
- [ ] Real-time notifications

## 🐛 **Troubleshooting**

### **Common Issues**

#### **Database Connection Failed**
```bash
# Check MySQL is running
brew services list | grep mysql

# Test connection
mysql -u pms_user -p pms_db

# Reset user permissions
mysql -u root -p -e "
  GRANT ALL PRIVILEGES ON pms_db.* TO 'pms_user'@'%';
  FLUSH PRIVILEGES;
"
```

#### **Docker Build Failed**
```bash
# Clean Docker cache
docker system prune -f

# Rebuild without cache
docker build --no-cache -t tolimoli/pms .
```

#### **Port Already in Use**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (if safe)
kill -9 <PID>

# Or use different port
SERVER_PORT=8081 mvn spring-boot:run
```

## 📊 **Performance**

### **Benchmarks**
- **Startup Time**: ~15-30 seconds
- **Memory Usage**: ~512MB (with 512MB heap)
- **Database Connections**: Max 10 connections
- **Response Time**: <100ms for basic operations

### **Optimization**
- Docker multi-stage builds for smaller images
- Database connection pooling
- JPA query optimization
- Caching for frequently accessed data

## 🤝 **Contributing**

### **Development Workflow**
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### **Code Standards**
- Java 17+ features
- Spring Boot best practices
- Clean Code principles
- Comprehensive testing
- JavaDoc documentation

### **Commit Convention**
```
feat: add new reservation management feature
fix: resolve database connection issue
docs: update API documentation
test: add unit tests for room service
refactor: improve code structure
```

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 **Acknowledgments**

- Spring Boot team for the excellent framework
- MySQL for reliable database solution
- Docker for containerization platform
- All contributors and testers

## 📞 **Support**

### **Documentation**
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)

### **Contact**
- **Email**: support@tolimoli.com
- **Issues**: [GitHub Issues](https://github.com/your-username/tolimoli-pms/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-username/tolimoli-pms/discussions)

---

**Made with ❤️ for the hospitality industry**

*Version: 1.0.0 | Last Updated: December 2024*