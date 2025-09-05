# ELECTRICITY BUSINESS - BACKEND

`Electricity Business` is a training project designed to learn how to prepare and build a fullstack
web app. This concept would allow a user to log into the app, rent their personal EV (Electric
Vehicle) charging station to users, and book other users' charging stations.

## Environment variables

### With IntelliJ IDEA

Set as environment variables or with your IDE configuration, or simply replace the variables in
`application-*.properties` files with your own values (but do **NOT** push to GitHub):

```yaml
  # App
  APP_BASE_URL=http://localhost:8080/api/v1

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
  JWT_ACCESS_TTL_MS=300000
  JWT_REFRESH_TTL_MS=604800000
  JWT_VERIFICATION_TTL_MS=2592000000
  JWT_PASSWORD_TTL_MS=900000
```

### With local environment variables
(ex: Linux `.bashrc`)

## Dependencies (soon)

## UML Class Diagram (soon)
