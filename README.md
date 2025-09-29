# ELECTRICITY BUSINESS - BACKEND

---
**Electricity Business** allows a user to log into the app, rent their personal EV (Electric
Vehicle) charging station to users, and book other users' charging stations.   
It features
authentication with email registration, finding an available station using geolocation or map
search, a booking system with confirmation/cancellation that works with a Stripe payment system,
allowing for cancellation, and many more!

> This is app is only a demo, designed as a training project for building fullstack web
> applications.

---

## Table of Content

<!-- TOC -->
* [ELECTRICITY BUSINESS - BACKEND](#electricity-business---backend)
  * [Table of Content](#table-of-content)
  * [Requirements](#requirements)
    * [Tools & versions](#tools--versions)
    * [Installation Steps](#installation-steps)
      * [Java, SDK](#java-sdk)
      * [MySQL (for production)](#mysql-for-production)
      * [Git](#git)
      * [Docker (development only)](#docker-development-only)
  * [Project details](#project-details)
    * [Dependencies](#dependencies)
    * [Configurations](#configurations)
    * [Environment variables](#environment-variables)
      * [Using IntelliJ IDEA](#using-intellij-idea)
      * [With local environment variables](#with-local-environment-variables)
      * [Other methods](#other-methods)
    * [Installation, run & build](#installation-run--build)
      * [Other commands](#other-commands)
  * [Deployment](#deployment)
    * [Procedure](#procedure)
    * [Rollback](#rollback)
  * [Other information](#other-information)
    * [UML Class Diagram](#uml-class-diagram)
<!-- TOC -->

---

## Requirements

This guide covers the information required to run, build & deploy this REST API application.

### Tools & versions

| Tool           | Version      |
|----------------|--------------|
| OS             | Ubuntu 24.04 |
| Java/SDK       | temurin-21   |
| MySQL (prod)   | 8.4          |
| Git            | 2.49         |
| Docker Desktop | 4.46         |

### Installation Steps

#### Java, SDK

[Install and manage versions of Java & JDK](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-22-04)

```shell
# Install JDK (contains JRE)
sudo apt install default-jdk

# Check version
java -version
javac -version

# Set environment variable

# Install Maven
sudo apt install maven

# Check version (if doesn't work, follow the guide to set the environment variables)
mvn -v
```

#### MySQL (for production)

[Install & Configure a MySQL server](https://ostechnix.com/how-to-install-mysql-in-ubuntu-linux/)

Since we'll be using a MySQL server on a Docker container for development, we don't need to install
a MySQL server locally in order to work on this project.  
For production (and in case of problem during development), here's the installation guide for a
MySQL server.

```shell
# Install MySQL server
sudo apt install mysql-server

# Check MySQL version
mysql --version

# Start server
sudo systemctl start mysql

# Stop server
sudo systemctl stop mysql

# Check server status
sudo systemctl status mysql

# Enable server auto-start on boot (use "disable" command instead to disable auto-start on boot)
sudo systemctl enable mysql

# Setup the mysql server (useful for production especially)
sudo mysql_secure_installation
```

#### Git

[Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

```shell
# Install git
sudo apt install git

# Check version
git --version
```

#### Docker (development only)

Here are two options:

- Docker Desktop: comes with a user interface, but heavier on the machine.
- Docker Engine: same functionalities, but lighter since it only requires using the CLI.

**Option 1: Docker Desktop**

[Install on Ubuntu Docker Desktop](https://docs.docker.com/desktop/setup/install/linux/ubuntu/)

Download the package [
*docker-desktop-amd64.deb*](https://desktop.docker.com/linux/main/amd64/docker-desktop-amd64.deb?utm_source=docker&utm_medium=webreferral&utm_campaign=docs-driven-download-linux-amd64)
or use the previous link for the latest version. Then follow these commands :

```shell
# Install the previously downloaded package (use the path to your downloaded package) (installs in: `/opt/docker-desktop`)
sudo apt-get install ./docker-desktop.amd64.deb
```

**Option 2: Docker Engine**

```shell
# Install Docker Engine
sudo apt install docker.io

# Check version
docker --version
```

**Commands**

Depending on which option you choose, Docker can then be used as a desktop application, or with the
following set of commands (similar to
MySQL server commands)

```shell
# Start Docker desktop
systemctl --user start docker-desktop

# Enable auto-start (disable with "disable" command)
systemctl --user enable docker-desktop

# Stop Docker desktop
systemctl --user stop docker-desktop
```

## Project details

### Dependencies

| GroupId         | Dependency              | Version | Type              |
|-----------------|-------------------------|:-------:|-------------------| 
| springframework | Spring Boot             |  3.5.5  | Framework         |
| springframework | Spring Web              |  3.5.5  | Framework/REST    |
| springframework | Spring Validation       |  3.5.5  | Dev/Validation    |
| springframework | Spring Data JPA         |  3.5.5  | ORM/Database      |
| mysql           | MySQL Connector         |  9.4.0  | Database          |
| springframework | Spring Security         |  3.5.5  | Authentication    |
| auth0           | JWT                     |  4.5.0  | Authentication    |
| springframework | Spring Mail             |  3.5.5  | Emailing          |
| springframework | Thymeleaf               |  3.5.5  | Template/Emailing |
| openhtmltopdf   | OpenHtmlToPdf           | 1.0.10  | PDF generation    |
| openhtmltopdf   | OpenHtmlToPdf PdfBox    | 1.0.10  | PDF generation    |
| springframework | Spring Test             |  3.5.5  | Tests             |
| springframework | Spring Security Test    |  3.5.5  | Tests             |
| springframework | Spring DevTools         |  3.5.5  | Development       |
| springframework | Spring Config Processor |  3.5.5  | Development       |
| springframework | Docker Compose (dev)    |  3.5.5  | Dev/Database      | (development only)
| springframework | Spring Actuator         |  3.5.5  | Dev/Monitoring    |
| springdoc       | OpenAPI WebMVC UI       |  2.1.0  | Dev/Documentation |

### Configurations

- **Compiler**: this project uses `Maven` to manage, build and deploy the application. The list of
  commands to start working with the app can be found in the
  section [Installation, run & build](#installation-run--build).
- **Database (dev only)**: because we're using the `docker-compose` dependency, SpringBoot will read
  the
  `compose.yaml` file when you run the application and automatically start a `MySQL` server on a
  `Docker container` (you must have Docker Desktop open in order to start the application).

### Environment variables

Since the goal is to deploy it and be able to test this app on a mobile device, this project uses
environment variables. Here are a few ways to set the environment variables to be able to use this
project.

#### Using IntelliJ IDEA

With Intellij IDEA, environment variables can be set directly in the IDE:

- Click on the `Run / Debug Configurations` button (next to the buttons `Run` & `Debug`) ->
  `Edit Configurations`
- Then allow environment variables in `Modify Options`.
- Now, write the variables below with your own values in the relevant field and IntelliJ will
  automatically read them when starting the app.

Note: profiles (`dev`, `prod`, etc.) are also set there and might need to be set up as well.

```yaml
  # The following values are examples and should not be used for live applications 

  # App 
  APP_BASE_URL=http://localhost:8080  # Value for local development

  # Database
  DATABASE_URL=localhost:3306
  MYSQL_DATABASE=db_electricity_business
  MYSQL_USER=dev
  MYSQL_PASSWORD=password
  MYSQL_ROOT_PASSWORD=root

  # Mail
  MAIL_HOST=<mail-hosting-service>
  MAIL_PORT=<mail-port>
  MAIL_USERNAME=<mail-hosting-service-username>
  MAIL_PASSWORD=<app-mail-password>

  # JWT (with readable duration, since Spring Boot 3.5+)
  JWT_ISSUER=<jwt-issuer-token>
  JWT_ACCESS=5m
  JWT_REFRESH=7d
  JWT_VERIFICATION=1d
  JWT_PASSWORD=15m
```

#### With local environment variables

If you're setting up a full environment (for example, deploying the app on your own server), you
might want to set these environment variables directly on your device. In this case, refer to your
operating system's documentation to set environment variables.

#### Other methods

Other methods to set environment variables exist, one of them would be to add a
`application-dev.properties` file, add it to the `.gitignore` file and add the environment variables
in plain text. Perhaps not the best implementation, but the easiest.

### Installation, run & build

Once you've installed the required tools, here is how you can start working on this project.

```shell
# Get the project from the GitHub repository:
https://github.com/mlangumier/electricity-business-backend.git

# Enter the project's folder
cd ./electricity-business-backend

# Install the dependencies
mvn clean install

# (optional) If the installation command failed, let's ignore the tests:
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

> Note: since your IDE probably has UI elements to do the same thing, you don't have to use maven
> commands to work on this project. However, they're often useful when working with an unfamiliar
> environment or encountering issues.

#### Other commands

Here are a few other commands that can be useful while working on this project.

**Maven**

```shell
# Download the dependencies (although maven should do it automatically with the install command)
mvn dependency:resolve

# Run the application with a specific profile (ex: dev)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Compile the app
mvn compile

# Start the implemented tests
mvn test

# Start integration tests
mvn verify

# Set a new version before release
mvn versions:set -DnewVersion=1.0.1

# Cleans the build folder, tests & builds a new executable JAR -> can found in "target/electricity-business-<version>.jar"
mvn clean package
```

**Docker**  
When encountering problems related to either Docker Desktop or the database, it can be useful to be
able to start/stop the MySQL container without interacting with Spring Boot.

```shell
# Start the container and display the logs
docker compose up

# Start the container in detached mode (don't display the logs, keep access to the shell)
docker compose up -d

# Stops the container
docker compose stop

# Stops and closes the container
docker compose down

# Stops and closes the container, and removes the image (clean slate)
docker compose down -v

# List all the running containers
docker ps

# Shows logs for the container
docker logs electricity-business-mysql
```

---

## Deployment

### Procedure

The deployment script we'll be using can be found in `./scripts/deploy.sh`.  
It allows us to deploy the application using the command below, automatically triggering the
following steps:

- Pull the changes from the repository (default branch: `main`)
- Install dependencies & run unit tests
- Packages the application into a new artifact
- Managing this & previous artifacts in `/releases` (for rollbacks)
- Start the application with the new `.jar`

> For now, the script must be run manually. It will be automated with the implementation of other
> tools later on.

Run the script with the following command:

```shell
# (optional) If you see the error: "Permission denied", use this command:
chmod +x ./scripts/deploy.sh

# Run the script with default values
./scripts/deploy.sh

# Run the script with some custom values (REPO_URL, APP_DIR, BRANCH, PROFILE)
APP_DIR=../test-deployment PROFILE=dev ./scripts/deploy.sh
```

> Note: right now, the script has an issue where it runs maven with Java 24 instead of Java 21 as
> set in the project. This needs to be solved before we can run the script without problem. See the
> snippet below for the exact error message.  
> Info: after testing & checks, the command `./mvnw -B test` runs properly within the project, but
> not from the script.
> - Project: the maven compiler (./mvnw) is explicitly set to version 21
> - Local environment: maven (mvn), java & javac are all set to version 21

```shell
[STEP-3] Installing dependencies & running tests...
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< fr.hb.mlang:electricity-business >------------------
[INFO] Building Electricity Business REST API 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:3.3.1:resources (default-resources) @ electricity-business ---
[INFO] Copying 3 resources from src/main/resources to target/classes
[INFO] Copying 0 resource from src/main/resources to target/classes
[INFO] 
[INFO] --- maven-compiler-plugin:3.14.0:compile (default-compile) @ electricity-business ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 1 source file with javac [debug parameters release 24] to target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.400 s
[INFO] Finished at: 2025-09-26T14:10:35+02:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.14.0:compile (default-compile) on project electricity-business: Fatal error compiling: error: release version 24 not supported -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
```

### Rollback

The rollback script can be found in `./scripts/rollback.sh`. Its purpose is to quickly deploy the
previous version of the application if an error occurs with the new one.   
Since the deployment script names the new artifact `current.jar` and the previous one
`previous.jar`, this rollback script simply swaps them and runs the previous deployment.

Run the script with the following command:

```shell
./scripts/rollback.sh
```

[//]: # (### Post-deploy verifications)

[//]: # (metrics & logs)

[//]: # (## Links)

[//]: # (repositories, host dashboards)

## Other information

### UML Class Diagram

Spécificities :

- Most entities will extend `AuditedEntity` in order to keep track of commonly logged fields such as
  `createdAt` or `updatedAt`.
- In our code, `User` will be separated into two entities: `User` and `UserAuth`, and
  `SecurityUserDetailsService` will implement `UserDetails` and have both entities as fields. That
  way, we keep both purposes of the entity separate while still being able to use them together.

![Class Diagram](/assets/class_diagram.png)
