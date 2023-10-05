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