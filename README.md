# Concert Ticket Management System
This Java-based application is designed to facilitate the management and booking of concert tickets. It offers functionalities for both administrators and customers, ensuring a smooth and efficient ticketing process.

## Overview

This project is a concert ticket management system designed to handle various aspects of ticket booking and management. The system is organized into different branches, each serving a specific purpose:

- **`main`**: Contains the core implementation and documentation.
  - Files: `README.md`, `TicketManagementEngine.java`
- **`feature`**: Integrates features from other branches and serves as the primary development branch.
- **`assets`**: Contains default information and assets used in the project.

# Features
## Admin Panel
- Add, update, and delete concert events
- Manage ticket inventory
- View sales reports and analytics
## Customer Interface
- Browse available concerts
- Book tickets
- View and manage bookings
## Technologies Used
- Java: Core programming language

## Object-Oriented Programming (OOP) Design

The project leverages Object-Oriented Programming principles to ensure a modular, scalable, and maintainable codebase. Key OOP concepts used include:

- **Encapsulation**: Data is encapsulated within classes, providing a clear interface for interacting with objects while hiding internal implementation details. For example, the `Ticket` class encapsulates ticket-related data and methods.

- **Inheritance**: The system uses inheritance to extend base classes into more specialized subclasses. For instance, `Admin` and `Customer` classes inherit from a common `User` base class, sharing common properties and behaviors.

- **Polymorphism**: Methods are designed to be polymorphic, allowing objects to be treated as instances of their parent class. This is used in the system to handle different types of users with a common interface.

- **Abstraction**: Abstract classes and interfaces define common methods and properties that derived classes must implement, promoting code reuse and reducing complexity. For example, the `FileHandler` interface abstracts file operations that are implemented differently by `FileHandlerImpl`.

# Usage
## Admin
1. Log in using admin credentials.
2. Use the admin panel to add, update, and delete concert events.
3. Manage the ticket inventory.
4. View sales reports and analytics.
## Customer
1. Register for a new account or log in with existing credentials.
2. Browse the list of available concerts.
3. Book tickets for the desired events.
4. View and manage your bookings.

# Build and Run
## Use Case 1: Customer mode with customer id and correct password 
$java TicketManagementEngine --customer 1 abc@1 ../assets/customer.csv ../assets/concert.csv ../assets/bookings.csv ../assets/venue_mcg.txt 
## Use Case 2: Customer mode with no customer id and password 
## Use Case 3: Admin Menu
$java TicketManagementEngine --admin ../assets/customer.csv ../assets/concert.csv ../assets/bookings.csv ../assets/venue_mcg.txt ../assets/venue_marvel.txt

# Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork this repository.
2. Create a new branch (git checkout -b feature-branch).
3. Make your changes and commit them (git commit -m 'Add some feature').
4. Push to the branch (git push origin feature-branch).
5. Open a pull request.
Please ensure your code follows the project's coding standards and includes appropriate tests.

