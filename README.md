# JJA - Java Integration Platform

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![Apache Camel](https://img.shields.io/badge/Apache%20Camel-4.10.2-red)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-Proprietary-lightgrey)

## 🚀 Overview

JJA is a robust, enterprise-grade integration platform built with Spring Boot and Apache Camel. This platform seamlessly connects disparate systems through a sophisticated service-oriented architecture, enabling real-time data exchange and report generation capabilities.

## ✨ Key Features

- **Multi-Tenant Architecture**: Designed for enterprise-scale deployments with isolated tenant contexts
- **Configurable Connections**: Dynamic connection management with secure credential storage
- **Report Generation**: Flexible report configuration with customizable formats and scheduling
- **SOAP Integration**: Enterprise-ready SOAP web service integration with Oracle systems
- **Message Queue Processing**: Asynchronous message processing via RabbitMQ
- **Containerized Deployment**: Docker-ready with optimized JVM settings for cloud environments

## 🛠️ Technology Stack

- **Java 21**: Leveraging the latest language features for optimal performance
- **Spring Boot 3.4**: Modern application framework with comprehensive ecosystem
- **Apache Camel**: Enterprise integration patterns implementation
- **Hibernate/JPA**: Robust ORM for database interactions
- **RabbitMQ**: Message broker for asynchronous communication
- **Oracle Database**: Enterprise-grade data persistence
- **Docker & Docker Compose**: Containerization for consistent deployment
- **Nginx**: High-performance reverse proxy and load balancer

## 🏗️ Architecture

The application follows a microservices-inspired architecture with clear separation of concerns:

```
JJA
├── Controllers (Web interface)
├── Integration Routes (Apache Camel)
├── Service Layer
├── Data Access Layer
└── Message Processing
```

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- Docker and Docker Compose
- Oracle Database instance (or configured connection)

### Quick Start

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/jja.git
   cd jja
   ```

2. Configure environment variables
   ```bash
   # Create a .env file with your configuration
   cp .env.example .env
   # Edit the .env file with your settings
   ```

3. Build and run with Docker Compose
   ```bash
   docker-compose up -d
   ```

4. Access the application
   ```
   http://localhost:8080
   ```

## 🔧 Configuration

The application supports extensive configuration through:

- Environment variables
- Spring profiles (dev, test, prod)
- External configuration files

## 📊 Monitoring

Health and metrics are available through Spring Boot Actuator:

- Health check: `/actuator/health`
- Metrics: `/actuator/metrics`

## 🔒 Security

- Secure credential storage
- HTTPS support via Nginx
- Authentication and authorization

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

## 📄 License

Proprietary - All rights reserved

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

*Built with ❤️ by the Integration Team*
