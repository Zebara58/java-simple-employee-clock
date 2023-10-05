# java-simple-employee-clock
An application that represents a simple time clock for employee shifts
The Spring Boot project was initially created using Spring Initializer (https://start.spring.io/)

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)

## Installation

Setup Development Environment:
- Ensure you have Java 17 installed (17.0.8.1 JDK originally used)
- Ensure you have Maven 3.9.4 installed
- Install MongoDB and start the MongoDB server
	- Database name: shifts
	- Collection name: employees
	- MongoDB port: localhost:27017

- Eclipse IDE setup: Install the Maven plugin (Maven (Java EE) Integration for Eclipse WTP)

## Usage

Eclipse Testing:
- Right-click on your project (shift-clock) and select Run As > shift-clock (Maven Build).

Eclipse Debug Testing:
- Right-click on your project (shift-clock) and select Run As > Java Application
- Select the ShiftClockApplication - com.example.shiftclock

Command Line Testing:
- Change your directory to the java-simple-employee-clock\Maven\shift-clock where the pom.xml file is
- run "mvn clean install"
- run "mvn spring-boot:run"

Example API Calls:
- Add Employee
POST http://localhost:8080/employees/add-employee
{
    "Id": "dc1e46c3-3d3d-4a47-9e4c-593f89feef40"
}

- Get Employee
GET http://localhost:8080/employees/get-employee/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Start Shift
POST http://localhost:8080/employees/start-shift/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Start Lunch
POST http://localhost:8080/employees/start-lunch/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Start Break
POST http://localhost:8080/employees/start-break/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Stop Lunch
POST http://localhost:8080/employees/stop-lunch/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Stop Break
POST http://localhost:8080/employees/stop-break/dc1e46c3-3d3d-4a47-9e4c-593f89feef40

- Stop Shift
POST http://localhost:8080/employees/stop-shift/dc1e46c3-3d3d-4a47-9e4c-593f89feef40