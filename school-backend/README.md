# School Backend Configuration

This document describes the configuration settings for the School Management Backend application, a Spring Boot application.

## Application Properties

The application configuration is defined in `src/main/resources/application.properties`. The following properties are configured:

### Application Name
- `spring.application.name=school`: Sets the name of the Spring Boot application.

### Server Port
- `#server.port=9000`: The server port is commented out, defaulting to 8080. Uncomment and modify if a different port is needed.

### Database Configuration
The application uses MySQL as the database and connects using environment variables for security:

- `spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?ssl-mode=REQUIRED&useSSL=true&trustServerCertificate=true`
  - Connects to a MySQL database using the specified host, port, and database name.
  - SSL is required with trust for server certificate.

- `spring.datasource.username=${DB_USER}`: Database username from environment variable.

- `spring.datasource.password=${DB_PASSWORD}`: Database password from environment variable.

- `spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`: Uses the MySQL Connector/J driver.

### Hibernate/JPA Configuration
- `spring.jpa.hibernate.ddl-auto=update`: Automatically updates the database schema based on entity changes.
- `spring.jpa.show-sql=true`: Logs SQL statements to the console for debugging.
- `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect`: Specifies the Hibernate dialect for MySQL.

## Environment Variables

To run the application, set the following environment variables:

- `DB_HOST`: The hostname or IP address of the MySQL database server.
- `DB_PORT`: The port number on which the MySQL server is running (default: 3306).
- `DB_NAME`: The name of the MySQL database to connect to.
- `DB_USER`: The username for database authentication.
- `DB_PASSWORD`: The password for database authentication.

### Example
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=school_db
export DB_USER=root
export DB_PASSWORD=yourpassword
```

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or compatible database server
- Maven 3.x for building the application

## Running the Application

1. Ensure MySQL is running and the database is created.
2. Set the required environment variables.
3. Build the application: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

The application will start on port 8080 (or the configured port).

## Docker

The application can also be run using Docker Compose, which handles the database setup and environment configuration automatically. Refer to the root `docker-compose.yml` for details.
