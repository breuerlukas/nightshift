# Nightshift

A self-hosted scheduler for AI agents. You describe a task once, give it a
schedule, and Nightshift runs an agent on it, like a cron job that thinks:
every morning, every hour, every first of the month. A panel shows what is
scheduled, what ran, what came out of it and what went wrong.

React and Tailwind in the panel, Spring Boot on Java 27 and Postgres behind it.

Nightshift is at an early stage and not usable yet.

|      | Build Status                                                                                                                                                         |
|------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| main | [![CI](https://github.com/breuerlukas/nightshift/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/breuerlukas/nightshift/actions/workflows/ci.yml) |

## Setup

You need Docker with Compose.

**1. Get the code**

```bash
git clone https://github.com/breuerlukas/nightshift.git
cd nightshift
cp .env.example .env
```

**2. Fill in `.env`**

```bash
DB_PASSWORD=$(openssl rand -hex 24)   # paste the output
PUBLIC_URL=http://localhost:8080
```

**3. Start**

```bash
docker compose up -d --build
```

**4. Open <http://localhost:8080>**

The panel only listens on `127.0.0.1`; the api and the database are never
published. To reach it from other devices, use a VPN or a reverse proxy with
HTTPS instead of forwarding the port.

## Everyday operation

| Task | Command |
|---|---|
| Update | `git pull && docker compose up -d --build` |
| Back up | `docker compose exec postgres pg_dump -U nightshift nightshift > nightshift.sql` |
| Restore | `docker compose exec -T postgres psql -U nightshift nightshift < nightshift.sql` |
| Logs | `docker compose logs -f api` |

## Development

| Part | Stack | Run |
|---|---|---|
| `api/` | Java 27, Spring Boot, PostgreSQL | `./gradlew bootRun` |
| `panel/` | React, TypeScript, Tailwind, shadcn/ui | `npm install && npm run dev` |

For a local database:
`docker run -d -p 127.0.0.1:5432:5432 -e POSTGRES_USER=nightshift -e POSTGRES_PASSWORD=nightshift -e POSTGRES_DB=nightshift postgres:18`.
The api connects to it without further configuration; in Docker it reads the
connection from the environment. The panel's dev server proxies `/v1` to
`localhost:8080`.

Checks: `./gradlew build` in `api/` compiles, lints (Checkstyle), looks for
bugs and security patterns (SpotBugs with FindSecBugs) and runs the tests;
`npm run lint && npm test` does the same for the panel.

## Contributing

Contributions are welcome. For larger changes please open an issue first (see
[CONTRIBUTING.md](CONTRIBUTING.md)). Everyone taking part is expected to follow
the [Code of Conduct](CODE_OF_CONDUCT.md).

## License

[AGPL-3.0](LICENSE.md)