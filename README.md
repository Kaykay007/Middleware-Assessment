##Middleware Technical Assessment

##Overview
This project is a mock RESTful API simulating a simple payment gateway middleware. The middleware facilitates transactions between a mock retail application and a banking service.

##Requirements

The project includes the following features:

-Endpoints for initiating a transaction: Allows clients to start a payment transaction.
-Endpoints for checking transaction status: Enables clients to check the status of a previously initiated transaction.
-Endpoints for receiving webhook notifications: Receives updates about transactions from the banking service.


## Features

- Initiate transactions between customer accounts
- Check the status of a transaction
- Receive webhook notifications for transaction updates
- Basic authentication for securing endpoints
- API documentation with Springdoc OpenAPI

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- Spring Boot 3.2.6
- MySQL

##Running Tests

-mvn test
