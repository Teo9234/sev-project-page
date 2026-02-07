# Clock-In System — A Full-Stack Project

This is a full-stack web app, that allows employees to clock in or out. It  includes a JWT authentication system. Built using Spring Boot for the back end and React + Vite for the back end.

---

## Table of Contents

- [Tech Stack](#List-of-Libraries--Technologies)
- [Prerequisites](#prerequisites)
- [1. Database Setup (MySQL)](#1-database-setup-mysql)
- [2. Setting up the Back End (Spring Boot)](#2-setting-up-the-back-end-spring-boot)
- [3. Setting up the Front End (React + Vite)](#3-setting-up-the-front-end-react--vite)
- [4. Running the entire app](#4-running-the-entire-app)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Default Credentials (Development)](#-default-credentials-development)

---

## List of Libraries & Technologies

| Layer      | Technology                                                                        |
| ---------- |-----------------------------------------------------------------------------------|
| Front End  | React 19, TypeScript, Vite 7, Tailwind CSS 4, React Router 7, Zod, React Hook Form |
| Back End   | Java, Spring Boot, Spring Security, Spring Data JPA, Gradle 8.14                  |
| Database   | MySQL 8+                                                                          |
| Auth       | JWT (JSON Web Tokens)                                                             |
| API Docs   | Swagger / OpenAPI 3 (SpringDoc)                                                   |

---

## Prerequisites

Make sure you have the following installed before starting:

| Tool        | Version    | Download Link                                                    |
| ----------- | ---------- | ---------------------------------------------------------------- |
| **Java JDK**| 17+        | [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/) |
| **Node.js** | 18+        | [nodejs.org](https://nodejs.org/)                                |
| **npm**     | 9+ (comes with Node) | Included with Node.js                                  |
| **MySQL**   | 8.0+       | [dev.mysql.com](https://dev.mysql.com/downloads/mysql/)          |
| **Git**     | Any        | [git-scm.com](https://git-scm.com/)                             |

---

## 1. Database Setup (MySQL)

1. Using MySQL Workbench create the database and the user with these SQL commands:

```sql
CREATE DATABASE IF NOT EXISTS clockin_db;
CREATE USER IF NOT EXISTS 'clockuser'@'localhost' IDENTIFIED BY 'clock123';
GRANT ALL PRIVILEGES ON clockin_db.* TO 'clockuser'@'localhost';
FLUSH PRIVILEGES;
```

2. In the back end's `src/main/resources/application-dev.properties` set `spring.jpa.hibernate.ddl-auto=update`, this way the back end connects to the database when running with the dev profile:

---

## 2. Setting up the Back End (Spring Boot)


Please note that the Gradle wrapper files are in the .gitignore. So you will need to generate like below:

You will need to create a build.gradle file and then generate it like so:

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.4.0'
    id 'io.spring.dependency-management' version '1.1.6'
}

group = 'com.clock_in'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0'
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
    runtimeOnly 'com.mysql:mysql-connector-j'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

Then also create `settings.gradle` like so:
```groovy
rootProject.name = 'clock-in'
```

And finally the wrapper:
```bash
gradle wrapper
```

Then execute this command::
```bash
.\gradlew.bat bootRun
```

### Back End will start on: **http://localhost:8080**

### Dev Profile

To run with the `dev` profile (uses environment-variable-based DB config):

```bash
.\gradlew.bat bootRun --args='--spring.profiles.active=dev'
```
---

## 3. Setting up the Front End (React + Vite)

```bash
# Change to the front end directory
cd front_end

# Install dependencies
npm install

# Start the development server
npm run dev
```

### The Front End will start on: **http://localhost:5173** (which is the Vite default)

> Because the front end call the back end API at `http://localhost:8080/api`, you have to start the back end before the front end.

---

## 4. Running the entire app

Open **two terminals** and run each one at the same time:

** First terminal for the back end:**
```bash
cd back_end
.\gradlew.bat bootRun
```

**Second terminal for the front end:**
```bash
cd front_end
npm install
npm run dev
```

Then open your browser of choice and navigate to **http://localhost:5173**.

---

## API Documentation (Swagger)


Once the back end is up and running, you can access the following:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Use the **Authorize** button in Swagger to add your JWT token (`Bearer <token>`) for authenticated endpoints.

---

## 🔑 Default Credentials (Development)

| Setting           | Value          |
| ----------------- | -------------- |
| MySQL Database    | `clockin_db`   |
| MySQL User        | `clockuser`    |
| MySQL Password    | `clock123`     |
| Back End Port     | `8080`         |
| Front End Port    | `5173`         |