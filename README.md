# SafetyNet Alerts API

## 📌 Project Description

SafetyNet Alerts is a Spring Boot REST API that provides emergency information for fire services.

The application reads data from a JSON file and exposes endpoints to retrieve information about persons, firestations, and medical records.

---

##  Architecture

The application follows a layered architecture:

Controller → Service → DataLoader → JSON file

- Controller: handles HTTP requests  
- Service: contains business logic  
- DataLoader: loads and saves data from JSON  
- DTOs: format API responses  

##  Technologies
-Java 17
-Spring Boot
-Maven
-JUnit
-JaCoCo
-SLF4J
-Jackson

##  Testing

The project includes:

- Unit tests for services  
- Controller tests  
- JaCoCo coverage report  
- Surefire test report  

**Coverage: 83%**

