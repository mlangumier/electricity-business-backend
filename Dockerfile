FROM eclipse-temurin:21 AS build

WORKDIR /app

COPY target/electricity-business-0.0.1-SNAPSHOT.jar /app/spring-api.jar

ENTRYPOINT ["java", "-jar", "spring-api.jar"]