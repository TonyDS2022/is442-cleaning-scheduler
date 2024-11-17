# Getting Started

### Pre-requisites
- [Liberica Standard JDK 21.0.4+9](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Maven](https://maven.apache.org/index.html)
  - Windows:
    - Install [Chocolatey](https://chocolatey.org/install?_gl=1*2j99bk*_ga*MTI1Mzk5MzE5MC4xNzI1MDM1MTkz*_ga_0WDD29GGN2*MTcyNTAzNTE5My4xLjEuMTcyNTAzNTIwMS4wLjAuMA..)
    - In an **Administrative** PowerShell, run the following:
      ```PowerShell
        choco install maven -y
      ```
    - Verify maven installation with
      ```PowerShell
        mvn --version
      ```
  - MacOS:
    - Open the terminal and run
      ```bash
        brew install maven
      ```
- PostgreSQL
  - Windows:
    - Download and run the [PostgreSQL installer from EDB](https://www.enterprisedb.com/software-downloads-postgres)
  - MacOS:
    - Download and install [Postgres App](https://postgresapp.com/)
    - Follow the instructions on the Postgres App website to initialize a new local server
  
  After you have successfully initialized a PostgreSQL server on your local environment, connect to it by running `psql` on your shell, and sequentially run the following:
  ```psql
    CREATE DATABASE is442_proj;
    GRANT ALL PRIVILEGES ON DATABASE "is442_proj" TO current_user;
    GRANT ALL PRIVILEGES ON DATABASE "is442_proj" TO postgres;
    CREATE EXTENSION postgis;
  ```
 - (optional) Obtain a Google Maps Platform API token from the [Google Cloud Console](https://console.cloud.google.com/). While we have provided some sample data in `src/main/resources`, if you wish to experiment with Location data that are not pre-cached, you will require a valid API token.
 - Set up your `.env` file in `src/main/resources` with the following:
  ```properties
  DB_USERNAME=<your postgres username>
  DB_PASSWORD=<your postgres password>
  GOOGLE_OD_API_KEY=<your google api token, leave blank if you don't have one>
  ```
  Replace `<YOUR_GOOGLE_MAPS_API_KEY>` with your Google Maps Platform API token.

### Companion front-end application
A Companion front-end application is housed in a different repository with its own README.md file. The repository can be found [here](https://github.com/mageshkarthiga/oop-cleaning-scheduler).

### Application Configuration
Configuration parameters are stored in `src/main/resources/*.properties` files. To run a fresh build of the application and persist data across sessions, comment out the following lines in `application.properties`:
```properties
spring.profiles.active=dev
```
And set `spring.jpa.hibernate.ddl-auto` to `create`:
```properties
spring.jpa.hibernate.ddl-auto=update
```
Other business logic configurations can be found in `src/main/resources/shift.properties` and `src/main/resources/leave-policy.properties`.

### Running the application
To run the application, execute the following command in the root directory of the project:
```bash
  mvn spring-boot:run
```

#### Documentation with Swagger
The accessible endpoints are documented using Swagger.

To access the Swagger documentation, navigate to `http://localhost:8080/swagger-ui.html` in your browser.