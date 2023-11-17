# Online Shopping System in Java

This is a console-based online shopping system implemented in Java with JDBC for database connectivity.

## Table of Contents
- [Setup](#setup)
  - [Create Database](#create-database)
  - [Update Database Connection](#update-database-connection)
- [Usage](#usage)
- [Database Connection Details](#database-connection-details)

## Setup

### Create Database

1. Create a new database in your MySQL server.
2. Execute the `database.sql` script provided in your database.

### Update Database Connection

Open the `DbConnector.java` file and modify the database URL, username, and password with your database details.

```java
private static final String URL = "jdbc:mysql://localhost:3306/your_database_name";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```
## Usage

Follow the on-screen instructions to register or log in. Explore available items and make purchases. Admins can access the admin panel for additional functionalities.

## Database Connection Details

Before running the application, make sure to set up the database by executing the 'Database.db' script. This script creates all the necessary tables and inserts sample values for testing.
