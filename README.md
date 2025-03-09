Product Price API

This is a Spring Boot REST API that allows querying product prices based on date, brand, and timezone.
It follows Hexagonal Architecture principles, using Spring Boot, Kotlin, H2 (in-memory database) for testing.

## 🚀 Running the Application

### 1️⃣ Prerequisites
- **JDK 21+** (Ensure Java 17 or later is installed)
- **Gradle**
- **cURL or Postman** (For API testing)

### 2️⃣ Run the Application

#### Using Gradle to start the application on port 8080
```sh
./gradlew bootRun
```

#### Call endpoint through cURL or Postman
```sh
curl --location 'http://localhost:8080/v1/products/35455/price?date=2020-06-14T10%3A00%3A00Z&brandId=1' \
--header 'x-timezone: GMT' \
--header 'Cookie: session_id=a0a6c8dd-e544-41d7-80fb-ff5f7238cbb3'
```
#### Or you can access the documentation through
```sh
http://localhost:8080/swagger-ui/index.html?configUrl=/api-specs/main.yaml
```


### 3️⃣ Run the tests
```sh
./gradlew test
```