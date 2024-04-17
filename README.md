# Summit-sync backend

## Run with docker compose
Example docker-compose.yml:

```yaml
services:
  db:
    image: postgres:16-bookworm
    environment:
      POSTGRES_PASSWORD: dev
      POSTGRES_USER: dev
      POSTGRES_DB: summitsync
    volumes:
      - pg-data:/var/lib/postgresql
    ports:
      - 5433:5432
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U dev -d summitsync"]
      interval: 5s
      timeout: 5s
      retries: 5
  redis:
    image: redis:7
    ports:
      - 6379:6379
  api:
    image: ghcr.io/summit-sync/backend:dev
    ports:
      - 8080:8080
    environment:
      - REDIS_HOST=redis
      - POSTGRES_HOST=db
      - POSTGRES_PORT=5432
    depends_on:
      db:
        condition: service_healthy
      redis:
        condition: service_started
volumes:
  pg-data:
```
To always have the latest version running please run a `docker compose pull`
before the `docker compose up`. Docker will not automatically check for a new version.

Images are rebuilt whenever a push on `main` occurs.
The following works well for me:
```shell
docker compose up -d
# when you want to update
docker compose pull
# automatically restarts all changed containers
docker compose up -d
```