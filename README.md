# Airtime API Project

## Overview

This project implements an API for user authentication using JWT and consumes the Airtime VTU API. The application includes functionality for user registration, login, and airtime purchases.

## Features

1. **User Authentication**: 
   - Register a new user.
   - Login and retrieve a JWT token.

2. **Airtime Purchase**: 
   - Purchase airtime through the Airtime VTU API.

3. **Unit Testing**:
   - Unit tests implemented with at least 50% code coverage.
  
 ##  API Endpoints
Authentication
1. **Register a User**:

POST /api/v1/auth/register
Request Body:
json
```
{
    "firstName": "yourFirstName",
    "lastName": "yourLastName",
    "username": "yourUsername",
    "password": "yourPassword"
}
```
Response:
json
```
{
   "message": "message",
    "username": "yourUserName"
}
```
2. Login And Get Token:

POST /api/v1/auth/login
Request Body:
json

{
    "username": "yourUsername",
    "password": "yourPassword"
}

Response:
json

{
   "message": "message",
    "token": "jwt_token"
}

3. Airtime Purchase
Purchase Airtime:
POST /api/v1/buyAirtime
Request Body:
json
{
    "requestId": "12362",
    "uniqueCode": "MTN_19399",
    "details": {
        "phoneNumber": "09132058051",
        "amount": 100
    }
}

This project includes unit tests for the controllers in the application, ensuring the functionality of the APIs and correct handling of errors.

### Controller Unit Tests

We have implemented unit tests for the following controllers:

- **AirtimeOrderController**: Tests for airtime purchase requests.

### Tools Used for Unit Testing

- **JUnit 5**: Framework for unit testing in Java.
- **Mockito**: Mocking framework used to simulate dependencies in the unit tests.
- **MockMvc**: Used for testing without needing to start the full HTTP server.
- **Gson**: Used to serialize and deserialize Java objects in JSON format.

### How to Run Unit Tests

To run the unit tests, use the following command in your project root directory:

```bash
./mvnw test


### Prerequisites

- Java 8 or higher
- Maven
- MySQL database (or H2 for in-memory testing)

### Installation

1. Clone the repository:
   ```bash
   git clone  https://github.com/bigboytimi/xpress-api.git
   cd xpress-api
