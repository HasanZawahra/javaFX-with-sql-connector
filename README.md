# JavaFX with SQL Connector

This repository provides a simple JavaFX application that connects to a MySQL database using a SQL connector. The application is designed to be used with a local database managed by phpMyAdmin using XAMPP.

## Setup

### Prerequisites
- Install XAMPP: Download XAMPP
- Ensure XAMPP is running, specifically Apache and MySQL.
- Database Setup
- Import the database schema:
- Open phpMyAdmin (http://localhost/phpmyadmin).
- Create a new database named avafx_database.
- Import the SQL file located in database/schema.sql into the newly created database.

## JavaFX Application
- Download the files.
- Open the project in your favorite Java IDE.
- Configure MySQL JDBC Driver:
1. Download the MySQL Connector/J: MySQL Connector/J
2. Add the MySQL Connector/J JAR file to your project's build path.

## Run the Application
- Locate the Main.java file.
- Run the Main class to start the JavaFX application.

## Features

- The JavaFX application connects to the MySQL database.
- The application retrieves and displays data from the database.
## Technologies Used

- JavaFX
- MySQL
- MySQL Connector/J
- XAMPP (for local database setup with phpMyAdmin)
## Contributors

Hasan Zawahra
