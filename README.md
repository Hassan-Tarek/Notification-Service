# Notification Service

A robust, scalable notification service built with Spring Boot, designed to handle multi-channel notifications including Email, SMS (via Twilio), and Push Notifications (via Firebase). This service uses an event-driven architecture with RabbitMQ to ensure high availability and reliability.

## 🚀 Features

- **Multi-channel Support**: Send notifications via Email, SMS, and Push.
- **Event-Driven Architecture**: Decoupled notification processing using RabbitMQ.
- **Persistence**: Stores notification history and status in MongoDB.
- **Scalable**: Dockerized and ready for deployment.
- **RESTful API**: Simple endpoints to trigger and retrieve notifications.

## 🛠️ Tech Stack

- **Core**: Java 17, Spring Boot 4+
- **Database**: MongoDB
- **Message Broker**: RabbitMQ
- **Integrations**:
  - **Email**: Java Mail Sender (SMTP)
  - **SMS**: Twilio
  - **Push**: Firebase Cloud Messaging (FCM)
- **Containerization**: Docker, Docker Compose
- **Tools**: Lombok, Maven

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Docker & Docker Compose](https://www.docker.com/products/docker-desktop/)
- [Maven](https://maven.apache.org/) (optional, included via wrapper)

## ⚙️ Configuration

The application relies on environment variables for sensitive configuration. You can set them in a `.env` file in the root directory.

### Environment Variables (`.env`)

Create a `.env` file in the project root with the following structure:

```env
# Server
SERVER_PORT=8080

# MongoDB
MONGODB_HOST=mongodb
MONGODB_PORT=27017
MONGODB_DATABASE=notification_db
MONGO_INITDB_ROOT_USERNAME=root
MONGO_INITDB_ROOT_PASSWORD=password

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_DEFAULT_USER=guest
RABBITMQ_DEFAULT_PASS=guest

# Email (Gmail SMTP example)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Twilio (SMS)
TWILIO_ACCOUNT_SID=your_twilio_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_FROM_NUMBER=your_twilio_number

# Firebase (Push)
FIREBASE_CONFIG_PATH=classpath:firebase/firebase-service-account.json
```

## 🚀 Running the Application

### Method 1: Docker Compose (Recommended)

This requires no manual local setup of MongoDB or RabbitMQ.

1.  **Build and Run**:
    ```bash
    docker-compose up -d --build
    ```
2.  The application will be available at `http://localhost:8080`.
3.  To stop:
    ```bash
    docker-compose down
    ```

### Method 2: Local Development

1.  **Start Infrastructure**:
    Ensure you have MongoDB and RabbitMQ running locally or use Docker for just the infra:
    ```bash
    docker-compose up -d mongodb rabbitmq
    ```
2.  **Run Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

## 🔌 API Endpoints

### 1. Send a Notification
**POST** `/api/v1/notifications/send`

```json
{
  "recipient": "+1234567890",
  "subject": "Welcome!",
  "content": "Thanks for signing up.",
  "type": "SMS" 
}
```
*Types: `SMS`, `EMAIL`, `PUSH`*

### 2. Get All Notifications
**GET** `/api/v1/notifications`

### 3. Get Notification by ID
**GET** `/api/v1/notifications/{id}`

## 🏗️ Architecture Flow

1.  **Client** sends a POST request to `/api/v1/notifications/send`.
2.  **Controller** receives the request and validates it.
3.  **Service** saves the notification with status `PENDING` to **MongoDB**.
4.  **Producer** sends the Notification ID to **RabbitMQ**.
5.  **Consumer** listens to the queue, picks up the ID, and retrieves full details from MongoDB.
6.  **Channel Resolver** determines the correct channel (SMS, Email, Push) and dispatches the message via the corresponding 3rd party API.
7.  **Service** updates the notification status to `SENT` or `FAILED` in MongoDB.
