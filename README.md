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

## Test Data

The database is pre-populated with test accounts:

| Account Number | Balance | Status  | Owner           |
|----------------|---------|---------|-----------------|
| 1000000001     | 1000    | ACTIVE  | Mirbek Atabekov |
| 1000000002     | 1000    | ACTIVE  | Temir Nazarov   |
| 1000000003     | 1000    | BLOCKED | Ivan Ivanov     |
| 1000000004     | 1000    | ACTIVE  | Sveta Svetikova |
| 1000000005     | 1000    | ACTIVE  | Petr Petrov     |
| 1000000006     | 1000    | BLOCKED | Michael Jackson |
| 1000000007     | 1000    | ACTIVE  | Naruto Uzumaki  |
| 1000000008     | 1000    | ACTIVE  | Optimus Prime   |

### Example transfer request
```json
{
  "fromAccountNumber": "1000000001",
  "toAccountNumber": "1000000002",
  "amount": 100
}
```