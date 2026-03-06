# Banking Transfer Service

Internal banking REST service for money transfers between accounts.

## Requirements

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Tech Stack

- Java 21
- Spring Boot 4.0
- PostgreSQL 17
- Flyway
- Docker

---

## Launch

### First launch (or after code changes)

```bash
docker-compose up --build
```

### Subsequent launches

```bash
docker-compose up
```

### Run in background

```bash
docker-compose up -d
```

The application will start at: `http://localhost:8080`

---

## Endpoints

| Method | URL                                       | Description                     |
|--------|-------------------------------------------|---------------------------------|
| POST   | `/api/transfers`                          | Transfer funds between accounts |
| GET    | `/api/accounts/{accountNumber}/statement` | Get account statement           |

### Statement query params

| Param  | Description | Example      |
|--------|-------------|--------------|
| `from` | Start date  | `2025-01-01` |
| `to`   | End date    | `2025-02-01` |
| `page` | Page number | `0`          |
| `size` | Page size   | `10`         |

---

## Database

To connect to the PostgreSQL container via pgAdmin or DBeaver:

```
Host:     localhost
Port:     5433
Database: banking_db
User:     postgres
Password: postgres
```

---

## Useful commands

```bash
# View application logs
docker logs -f banking-transfer-service

# View database logs
docker logs -f banking

# Stop containers (data is preserved)
docker-compose down

# Stop containers and delete all data
docker-compose down -v
```

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`