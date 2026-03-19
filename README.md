# Agentic Dev — Multi-Module Monorepo

A learning project to build a microservices-style application using an AI Agent (Claude Code).
Follows Maven multi-module architecture with Spring Boot 3 and automated CI via GitHub Actions.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4+**
- **Maven** (multi-module)
- **GitHub Actions** (CI)

## Project Structure

```
agentic-dev/                  ← Parent POM (this repo)
└── account-service/          ← (coming soon) First microservice module
```

## Modules

| Module | Description | Status |
|---|---|---|
| `account-service` | Account management with soft-delete | Planned |

## Getting Started

### Prerequisites
- JDK 21+
- Maven 3.9+

### Build
```bash
mvn clean install
```

### CI
Every push to `master` triggers the GitHub Actions workflow (`.github/workflows/ci.yml`)
which runs `mvn clean verify` on JDK 21.

## Jira
Epic: [KAN-4] Agentic Dev Multi-Module Monorepo
