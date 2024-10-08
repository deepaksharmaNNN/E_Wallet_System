# E-Wallet System

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [Contact](#contact)

## Introduction

The E-Wallet System is a microservices-based application developed using Java and Spring Boot, designed to manage seamless transactions. This project incorporates user authentication, authorization, and transaction management via RESTful APIs, with Apache Kafka utilized for message processing.

## Features

- **User Authentication and Authorization:** Secure login and registration processes with Spring Security.
- **Transaction Management:** RESTful APIs for handling transactions.
- **Microservices Architecture:** Each service operates independently for better scalability and maintainability.
- **Real-time Message Processing:** Utilizes Kafka for efficient message handling.
- **Error Handling and Logging:** Comprehensive error management and logging mechanisms.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Apache Kafka**
- **MySQL**
- **Feign Client**
- **Gradle**
- **IntelliJ IDEA**

## Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/deepaksharmaNNN/E_Wallet_System.git
2. **Run the application:**
   
    Ensure that you have a MySQL database set up and configured in application.properties.
    Start Kafka using Docker:
   ```bash
    docker run --name kafka-container -p 9092:9092 -d bitnami/kafka
   ```
   Use IntelliJ IDEA to run the Spring Boot application.

## Usage

- **API Endpoints:**

  ### User Endpoints
  - **Create User:** 
    - **Method:** `POST`
    - **Endpoint:** `/user`
    - **Request Body:** 
      ```json
      {
        "phoneNo": "string",
        "name": "string",
        "email": "string",
        "password": "string"
      }
      ```

  - **Get User by Phone Number:** 
    - **Method:** `GET`
    - **Endpoint:** `/user`
    - **Query Parameters:** 
      - `phoneNo`: The phone number of the user.
    
  ### Transaction Endpoints
  - **Initiate Transaction:** 
    - **Method:** `POST`
    - **Endpoint:** `/transaction`
    - **Request Body:** 
      ```json
      {
        "amount": "number",
        "recipientPhoneNo": "string",
        "description": "string"
      }
      ```

  - **Get All Transactions:** 
    - **Method:** `GET`
    - **Endpoint:** `/transaction/all`
    - **Query Parameters:** 
      - `pageNo`: The page number for pagination.
      - `pageSize`: The number of transactions per page.

## API Documentation

For detailed API documentation, you can use tools like Swagger or Postman. Ensure to provide sample requests and responses for better clarity.

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository.**
2. **Create a new branch:**
   ```bash
   git checkout -b feature-branch
3. **Commit your changes:**
    ```bash
    git commit -m "Add new feature"
4. **Push to the branch:**
    ```bash
    git push origin feature-branch
5. **Create a Pull Request.**

## Contact

For any queries, feel free to reach out:

- **Name:** Deepak Sharma
- **Email:** dsharma2828@gmail.com
- **GitHub:** [deepaksharmaNNN](https://github.com/deepaksharmaNNN)
