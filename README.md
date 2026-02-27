# Social Network for Ducks & Humans

A desktop social networking application developed as part of an Advanced Programming Methods course. The project focuses on applying layered architecture and design patterns to ensure code maintainability and clear separation of concerns.

---

## Key Features

### For Users (Ducks & Humans):
* **Authentication System:** Secure login with encrypted passwords.
* **Friendship Management:** Send, accept, or decline friend requests.
* **Real-time Communication:** Private messaging system between friends.
* **Notifications:** Alert system for activities and incoming friend requests.
* **Events:** Discover and participate in events.

### For Administrators:
* **Full Control:** Complete management of the user database.
* **Advanced Data Visualization:** Tables with filtering and pagination for user data.
* **Audit:** Monitoring of platform activity.

---

## Architecture and Design Patterns

The project was built using **Clean Coding** and **Domain-Driven Design (DDD)** principles, structured into a **Layered Architecture**:
1. **UI Layer:** JavaFX (Graphical Interface).
2. **Service/Business Layer:** Core application logic.
3. **Repository Layer:** Data persistence using PostgreSQL.
4. **Domain Layer:** Data models and entities.

### Implemented Design Patterns:
* **Observer Pattern:** Used for automatic UI synchronization when data is modified.
* **Factory Pattern:** Used for consistent instantiation of complex entities.
* **Strategy Pattern:** Used for various utility classes and entity validation strategies.

---

## Tech Stack

* **Language:** Java
* **UI Framework:** JavaFX
* **Build Tool:** Gradle
* **Database:** PostgreSQL
* **IDE:** IntelliJ IDEA

---

## Project Goal
This project was developed as part of the **Advanced Programming Methods** course to deepen the understanding of software architecture and object-oriented design.

