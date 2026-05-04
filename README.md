# 🏦 JPMorgan Chase & Co. - Midas Core Service

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white" alt="Kafka" />
  <img src="https://img.shields.io/badge/H2_Database-4A148C?style=for-the-badge&logo=sqlite&logoColor=white" alt="H2 DB" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
</div>

<br />

Welcome to the **Midas Core** project, developed as part of the **JPMorgan Chase Software Engineering Virtual Experience**. Midas is a high-volume financial transaction processing system, and Midas Core is the central backend service responsible for receiving, validating, recording, and exposing financial transactions in real-time.

---

## 🌟 Executive Summary

This project demonstrates an enterprise-grade backend architecture utilizing message queuing, relational database persistence, and external REST API integrations. The system handles asynchronous transaction processing securely, ensuring data integrity and enforcing financial business rules.

### Key Architecture Components:
- **Asynchronous Ingestion:** Consumes real-time transactions via **Apache Kafka** to decouple frontend services and handle high load without bottlenecks.
- **Relational Persistence:** Uses **H2 Database** via **Spring Data JPA** to guarantee ACID properties during transaction processing.
- **Microservices Communication:** Integrates with an external Incentive REST API via **RestTemplate** to calculate transaction bonuses dynamically.
- **Data Exposure:** Provides a robust RESTful endpoint to securely expose calculated user balances to other microservices.

---

## 🛠️ Implementation Milestones

### Task 1: Environment & Dependency Setup
- Configured a robust **Spring Boot** application utilizing **Java 17**.
- Pinned highly stable enterprise dependencies in `pom.xml`, including `spring-boot-starter-web`, `spring-kafka`, `spring-boot-starter-data-jpa`, and `h2`.
- Validated initial application configuration and context loading via automated test suites.

### Task 2: Apache Kafka Integration
- Engineered a `@KafkaListener` to consume real-time messages from the `trader-updates` topic.
- Implemented robust JSON deserialization to parse raw Kafka streams into strongly-typed `Transaction` domain models.

### Task 3: Financial Rules & H2 Database Persistence
- Modeled the relational database schema using JPA `@Entity`, establishing `@ManyToOne` relationships between `TransactionRecord` and `UserRecord`.
- Implemented strict business logic validation:
  - Ensured both Sender and Recipient exist.
  - Validated sufficient funds (`sender.balance >= transaction.amount`) before execution.
- Maintained data integrity using Spring's `@Transactional` boundary to prevent partial updates.

### Task 4: External API Integration (Incentives)
- Simulated a microservices architecture by integrating an external "Incentive API".
- Used `RestTemplate` to send HTTP POST requests with validated transactions and retrieve calculated incentive bonuses.
- Modified the transaction flow to securely credit the incentive amount *only* to the recipient without penalizing the sender.

### Task 5: REST Controller & Data Exposure
- Developed a `@RestController` to expose the `/balance` GET endpoint.
- Handled incoming requests using `@RequestParam`, securely querying the underlying `DatabaseConduit` to return real-time user balances.
- Returned gracefully modeled JSON responses (or default zero balances for invalid users).

---

## ⚙️ How to Run Locally

### Prerequisites
- Java 17
- Apache Maven 3.9+
- A running instance of the Incentive API (`services/transaction-incentive-api.jar`)

### Build & Execute
1. **Start the Incentive API (Background Process)**
   ```bash
   java -jar services/transaction-incentive-api.jar &
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Automated Verification Tests**
   ```bash
   mvn -Dtest=TaskOneTests test
   mvn -Dtest=TaskTwoTests test
   mvn -Dtest=TaskThreeTests test
   mvn -Dtest=TaskFourTests test
   mvn -Dtest=TaskFiveTests test
   ```

---

## 📈 Learning Outcomes

Through building Midas Core, the following skills were mastered:
- 🚀 **Decoupling architectures** using Message Queues (Kafka).
- 🛡️ **Defensive Programming** and enforcing data integrity with Spring Data JPA.
- 🔗 **RESTful API consumption and creation** within a microservices ecosystem.
- 🧪 **Test-Driven Development (TDD)** using embedded Kafka containers (`@EmbeddedKafka`) and Spring Boot Test.

---
<div align="center">
<i>Built with passion to engineer the future of finance. 💼</i>
</div>
