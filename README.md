# 📡 Real-Time Distributed Notification System

A fully event-driven, microservices-based **real-time notification platform** built with **Java**, **Spring Boot**, **Kafka**, **Redis**, **MongoDB**, and **WebSockets**.

This project demonstrates modern distributed-systems patterns used by platforms like **Slack**, **Discord**, **Uber**, and **Airbnb**.

It supports:

* In-app **real-time notifications** via WebSockets
* Persistent **notification history** via MongoDB
* **Event-driven fan-out** using Kafka
* **Redis Pub/Sub** for scalable real-time broadcast
* **JWT-authenticated WebSocket sessions**
* Horizontal scalability

---

## 🚀 Architecture Overview

```
┌─────────────────────────┐       POST /notifications
│ Notification API Service│─────────────────────────────────────┐
└───────────────┬─────────┘                                     │
                │ produces                                      ▼
                ▼                                       notification-requests
       Kafka Topic: notification-requests   ┌──────────────────────────────────────┐
                                            │   Notification Processor Service     │
                                            │  - Fan-out per user/channel          │
                                            │  - Builds NotificationCreated events │
                                            └───────────────┬──────────────────────┘
                                                            │ produces
                                                            ▼
                                             Kafka Topic: notification-events
                                                            │
                                        ┌────────────────────▼───────────────────┐
                                        │    Notification Store Service          │
                                        │  - Consumes NotificationCreated        │
                                        │  - Stores into MongoDB                 │
                                        │  - Publishes lightweight JSON to Redis │
                                        └───────────────┬────────────────────────┘
                                                        │ publishes
                                                        ▼
                                          Redis Channel: in-app-notifications
                                                        │
                                           ┌────────────▼────────────┐
                                           │    WebSocket Gateway    │
                                           │ - Subscribes Redis      │
                                           │ - Manages WS sessions   │
                                           │ - Pushes real-time msgs │
                                           └─────────────┬───────────┘
                                                         │
                                                         ▼
                                              Connected Users (Browser/App)
```

---

## 🧩 Microservices

### 1️⃣ Notification API Service

* Accepts REST requests to create notifications
* Validates payloads
* Publishes `NotificationRequest` events to Kafka

### 2️⃣ Notification Processor Service

* Reads `NotificationRequest`
* Performs fan-out per recipient
* Creates `NotificationCreated` events
* Determines channels
* Publishes results to Kafka

### 3️⃣ Notification Store Service

* Consumes `NotificationCreated`
* Stores notifications into MongoDB
* Publishes lightweight JSON to Redis
  (for real-time delivery)

### 4️⃣ WebSocket Gateway Service

* Exposes **/ws?token=JWT**
* Authenticates users using JWT
* Maintains user → WebSocket session mappings
* Subscribes to Redis Pub/Sub
* Pushes notifications instantly to online users

---

## 🧠 Key Concepts Demonstrated

### ✔ Event-Driven Architecture

All communication between services is asynchronous via Kafka.

### ✔ CQRS-style separation

* **Write side** → API + Processor
* **Read side** → Store

### ✔ Real-Time WebSockets

Users receive notifications instantly without polling.

### ✔ Redis Pub/Sub Fan-Out

Low-latency real-time broadcast to horizontally scalable WebSocket gateways.

### ✔ Service Decoupling

Each service is independent, deployable, and scalable.

### ✔ JWT Auth + WebSockets

Secure real-time connection model.

### ✔ MongoDB Materialized Views

Fast lookup for user notifications & read/unread state.

### ✔ Kafka as durable event log

Reliable delivery between services.

---

## 🛠 Technologies Used

| Layer     | Tech                                              |
| --------- | ------------------------------------------------- |
| Language  | **Java 17**                                       |
| Framework | **Spring Boot 3**                                 |
| Messaging | **Apache Kafka**, **Kafka Schema Registry**, Avro |
| Real-Time | **WebSockets**, **Redis Pub/Sub**                 |
| Storage   | **MongoDB**                                       |
| Auth      | **JWT**                                           |
| Build     | **Maven**                                         |
| Infra     | **Docker Compose**                                |

---

## 🧪 Running the System Locally

### 1. Clone the repo

```bash
git clone https://github.com/burakmert236/real-time-notification-system.git
cd real-time-notification-system
```

---

## 2. Generate Avro Schemas

Inside `avro-schemas` module:

```bash
mvn clean install
```

This will generate Java classes for:

* `NotificationRequest`
* `NotificationCreated`

---

## 3. Build all services

From project root:

```bash
mvn clean package -DskipTests
```

---

## 4. Start infrastructure (Kafka, Redis, Mongo)

```bash
docker-compose up -d kafka zookeeper schema-registry redis mongo
```

---

## 5. Start microservices (in any terminal or via your IDE)

Example:

```bash
docker compose up -d
```


---

## 6. Test With a Notification Request

POST to API:

```bash
curl -X POST http://localhost:8111/notifications \
-H "Content-Type: application/json" \
-d '{
  "type":"COMMENT",
  "actorId":"user-123",
  "recipientIds":["user-42"],
  "title":"New comment",
  "body":"Alice commented on your post",
  "data":{"postId":"p-100"}
}'
```

Flow:

```
API → Kafka → Processor → Kafka → Store → Mongo + Redis → WebSocket Gateway → Browser
```

---

## 7. Connect WebSocket Client

Example JavaScript client:

```javascript
const token = "<your_jwt_here>";
const socket = new WebSocket("ws://localhost:8114/ws?token=" + token);

socket.onmessage = evt => console.log("Notification:", JSON.parse(evt.data));
socket.onopen = () => console.log("Connected!");
socket.onclose = () => console.log("Disconnected!");
```

As soon as a notification is created → it appears in the console.

---

## 📊 Example MongoDB Document

```json
{
  "_id": "notif-123",
  "recipientId": "user-42",
  "title": "New comment",
  "body": "Alice commented on your post",
  "data": {"postId": "p-100"},
  "channel": "IN_APP",
  "read": false,
  "createdAt": 1700000000000
}
```
