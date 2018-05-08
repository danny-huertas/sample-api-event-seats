# Spring Boot Event Seats REST API

### This application is an assessment project demonstrating how to build a REST API using SpringBoot.

The REST API can return the to return the following.
* Total count of seats available for a given event.
* Seat count based on seats available for a given event.
* Seat count based on type for a given event.
* Seat count based on aisle for a given event.

## Prerequisites
- JDK 1.8+
- Gradle 3+

## Stack
- Spring Boot
- Liquibase
- Swagger
- H2

#### How to Run?
Run EventSeatsApplication.java as a Java application.

#### Health Check
To view a health check of the application go to http://localhost:3020/health.

#### Swagger
To explore and invoke the REST endpoints go to http://localhost:3020/swagger-ui.html.

#### H2 Database 
The databases is built using liquibase.  It's also loaded with 4 event id's (1, 2, 3 and 4).  Each event contains a different amount of seats. 
To view the database console go to http://localhost:3020/console/login.jsp.  From the console, you can run queries to view / modify data.  

#####The database connection info is:
* JDBC URL: jdbc:h2:mem:~/ticketmaster;
* User Name: ticketmaster
* Password: ticketmaster
* Driver Class: org.h2.Driver

#### Dsl Seed Job
The dsl seed can be ran on a Jenkins server to create build / deploy jobs for this API.  The deploy jobs can create snapshot as well as release artifacts for this API.

#### Common package
The code in the common package can be moved to a separate repo and included as a dependency in this API (as well as others).