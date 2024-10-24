# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.3/maven-plugin/build-image.html)
* [Spring Web](https://spring.io/projects/spring-framework)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Requirements
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
- (optional, recommended) [Intellij IDEA Ultimate](https://www.jetbrains.com/community/education/#students/)
- PostgreSQL
  - Windows:
    - Download and run the [PostgreSQL installer from EDB](https://www.enterprisedb.com/software-downloads-postgres)
  - MacOS:
    - Download and install [Postgres App](https://postgresapp.com/)
    - Follow the instructions on the Postgres App website to initialize a new local server

  After you have successfully initialized a PostgreSQL server on your local environment, connect to it by running `psql` on your shell, and run the following:
  ```psql
    CREATE DATABASE is442_proj;
    GRANT ALL PRIVILEGES ON DATABASE "is442_proj" TO current_user;
    GRANT ALL PRIVILEGES ON DATABASE "is442_proj" TO postgres;
    CREATE EXTENSION postgis;
  ```

### Push policy and CI Actions
Please test compile the application with `mvn test` before pushing to the repository.