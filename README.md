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
    * [Verifications](#verifications)
    * [Rollback plan](#rollback-plan)
  * [Links](#links)
  * [Other information](#other-information)
    * [UML Class Diagram](#uml-class-diagram)
<!-- TOC -->

---

## Requirements

This guide covers the information required to run, build & deploy this REST API application.

### Tools & versions

| Tool             | Version      |
|:-----------------|--------------|
| OS               | Ubuntu 24.04 |
| npm              | 11.6         |
| Node             | 22.19        |
| SDK              | openjdk-24.0 |
| MySQL (optional) | 8.4          |
| Git              | 2.49         |
| Docker Desktop   | 4.46         |

### Installation Steps

[//]: # (TODO: Replace the links with the specific steps to follow)

- [Npm & Node](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
- [Java/SDK](https://docs.oracle.com/en/java/javase/23/install/overview-jdk-installation.html)
- [MySQL](https://ostechnix.com/how-to-install-mysql-in-ubuntu-linux/) [^1]
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- [Docker Desktop](https://docs.docker.com/desktop/setup/install/linux/ubuntu/)

[^1]: Since we'll be using a MySQL server container in Docker, we don't need to install a MySQL
server locally.

## Project details

### Dependencies

| GroupId         | Dependency              | Version | Type              |
|-----------------|-------------------------|:-------:|-------------------| 
| springframework | Spring Boot             |  3.5.5  | Framework         | (default)
| springframework | Spring Web              |  3.5.5  | Web server        |
| springframework | Spring Validation       |  3.5.5  | ORM               |
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
| springframework | Docker Compose          |  3.5.5  | Dev/Database      |
| springframework | Spring Actuator         |  3.5.5  | Dev/Monitoring    |
| springdoc       | OpenAPI WebMVC UI       |  2.1.0  | Dev/Documentation |

### Configurations

- **Compiler**: this project uses `Maven` to manage, build and deploy the application. The list of
  commands to start working with the app can be found in the
  section [Installation, run & build](#installation-run--build).
- **Database**: because we're using the `docker-compose` dependency, SpringBoot will read the
  `compose.yaml` file when you run the application and automatically start a `MySQL` server on a
  `Docker` container (you must have Docker Desktop open in order to start the application).

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
  # The following values are examples and not be used for live applications 

  # App
  APP_BASE_URL=http://localhost:8080

  # Database
  MYSQL_DATABASE=db_electricity_business
  MYSQL_USER=dev
  MYSQL_PASSWORD=password
  MYSQL_ROOT_PASSWORD=root

  # Mail
  MAIL_HOST=<mail-hosting-service>
  MAIL_PORT=<mail-port>
  MAIL_USERNAME=<mail-hosting-service-username>
  MAIL_PASSWORD=<app-mail-password>

  # JWT
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

# Set a new version before release
mvn versions:set -DnewVersion=1.0.1

# Cleans the build folder, tests & builds a new executable JAR file in "target/electricity-business-<version>.jar"
mvn clean package

# Install the JAR file into a local repository
mvn install
```

**Docker**  
When encountering problems related to either Docker Desktop or the database, it can be useful to be
able to start/stop the MySQL container without interacting with Spring Boot.

```shell
# Start the container and display the logs
docker compose up

# Start the container in detached mode (don't display the logs, keep access to the shell)
docker compose up -d

# List all the running containers
docker ps

# Stops the container
docker compose stop

# Stops and closes the container
docker compose down

# Stops and closes the container, and removes the image (clean slate)
docker compose down -v
```

---

[//]: # (## Deployment)
[//]: # (TODO: think about & prepare deployment setup & procedures)

[//]: # (### Procedure)
[//]: # (how & where to deploy)

[//]: # (### Verifications)
[//]: # (metrics & logs)

[//]: # (### Rollback plan)
[//]: # (how is the rollback managed & to what version/build)

[//]: # (## Links)
[//]: # (repositories, host dashboards)

[//]: # (## Other information)

[//]: # (### UML Class Diagram)

[//]: # (TODO: Add class diagram)
