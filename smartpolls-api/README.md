# Smart Polls API

Spring Boot backend for the Smart Polls app. Requires **PostgreSQL** to be running before you start the API.

## Run the backend

### 1. Start PostgreSQL (required)

From this directory, start the database with Docker:

```bash
docker-compose up -d
```

Wait a few seconds for Postgres to be ready. If you see **"Connection to localhost:5432 refused"** when running the API, the database is not running — run the command above first.

### 2. Start the API

```bash
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API will be available at **http://localhost:8080**.

## Configuration

- **Database:** `localhost:5432`, database `smartpolls`, user/password `smartpolls` (see `application.yaml` and `docker-compose.yml`).
