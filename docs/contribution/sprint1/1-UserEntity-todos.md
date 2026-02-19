
# Todos: Phase 1 User Entity & Role Management

Purpose: this file contains the steps on how to setup the project for the first sprint.

## **1.1 User Entity & Role Management**

Matthew - COMPLETED
### Create Role Enum

Matthew - COMPLETED
### Create User Entity

Matthew - COMPLETED
### Add Validation Annotations

Matthew - COMPLETED
### Generate Boilerplate Code


## **1.2 Database Configuration**

Who?
### Configure application.properties
- [ ] Open `src/main/resources/application.properties`
- [ ] Add datasource URL (H2/MySQL/PostgreSQL)
    - Example for H2: `spring.datasource.url=jdbc:h2:mem:cookiegram`
- [ ] Add datasource username: `spring.datasource.username=`
- [ ] Add datasource password: `spring.datasource.password=`
- [ ] Add driver class name (if not auto-detected): `spring.datasource.driver-class-name=`

Who?
### Configure JPA/Hibernate Properties
- [ ] Set DDL auto strategy: `spring.jpa.hibernate.ddl-auto=update` (or `create-drop` for dev)
- [ ] Enable SQL logging: `spring.jpa.show-sql=true` (optional, for debugging)
- [ ] Format SQL output: `spring.jpa.properties.hibernate.format_sql=true` (optional)
- [ ] Set database dialect: `spring.jpa.properties.hibernate.dialect=` (if needed)
- [ ] Configure H2 console (if using H2): `spring.h2.console.enabled=true`

Who? 
### Verify Database Connection
- [ ] Add database dependency to `pom.xml` or `build.gradle` (H2, MySQL, PostgreSQL driver)
- [ ] Test application startup to ensure database connection is successful
- [ ] Check logs for any database connection errors
- [ ] Verify schema is created (check H2 console or database client)


## **1.3 User Repository Layer**

Matthew - COMPLETED
### Create UserRepository Interface

Matthew - COMPLETED
### Add Custom Query Methods