# CookigramStudios - Capstone Project

## CookieGram Docker + CI/CD Playbook

Hey team, this guide will help you run Docker on you're own machine. 

You can probably see (or not see it), I added two new GitHub Actions Secrets which configs the Docker-Publish.

## Why image naming changed

During development, we used:

- `hainesksc/cookiegramproject:experimental`

That tag/repo was fine for local dev experimentation, but it was not our finalized CI publish target.

For final delivery, I configured GitHub Actions using my Docker Hub account (I was assigned the Docker setup task), so published images now go to:

- `lobsterchop/cookiegramproject`

This is why we standardized demo/release instructions on `lobsterchop/cookiegramproject`.

Important context:
- `hainesksc/cookiegramproject:experimental` = earlier development image naming.
- `lobsterchop/cookiegramproject` = current CI/CD publishing source of truth for this project.

Benefits of standardizing:

1. One canonical image source for the team and demo.
2. GitHub Actions output matches exactly what teammates/judges pull.
3. Clear separation between old dev/experimental tags and final demo/release tags.

### Prerequisites

1. Docker Desktop installed and running.
2. Access to this GitHub repo.
3. Access to Docker Hub image repo: `lobsterchop/cookiegramproject`.
4. Ports available:
- `8080` for app
- `3307` for MySQL

### Local environment setup

1. Ensure you have the `.env` file please!!! 
2. Fill `.env` with local dev values.
3. Never commit `.env` with real credentials.

You should already have this, so skip it you've done so.

Example required keys:

- `SPRING_PROFILES_ACTIVE`
- `APP_BOOTSTRAP_USERS_ENABLED`
- `BOOTSTRAP_ADMIN_EMAIL`
- `BOOTSTRAP_ADMIN_PASSWORD`
- `BOOTSTRAP_EMPLOYEE_EMAIL`
- `BOOTSTRAP_EMPLOYEE_PASSWORD`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `MYSQL_ROOT_PASSWORD`

## Compose files and purpose

1. `docker-compose.yml`
- Local development flow.
- Can build app from source.

2. `docker-compose.demo.yml`
- Demo/release flow.
- Pulls app from Docker Hub image:
- `lobsterchop/cookiegramproject:${APP_IMAGE_TAG:-latest}`

## Run locally (build from source)

```bash
docker compose up -d
docker compose ps
```

Expected:

1. `cookiegram-mysql` is `healthy`.
2. `cookiegram-app` is `Up`.
3. App is reachable at `http://localhost:8080`.

## Run demo mode (pull from Docker Hub)

```bash
docker compose -f docker-compose.demo.yml up -d
docker compose -f docker-compose.demo.yml ps
```

Expected:

1. MySQL healthy.
2. App running.
3. App works at `http://localhost:8080`.

### Run a fixed release tag (recommended for demos)

#### PowerShell

```powershell
$env:APP_IMAGE_TAG="v1.0.0-demo"
docker compose -f docker-compose.demo.yml up -d
```

#### Bash/macOS/Linux

```bash
APP_IMAGE_TAG=v1.0.0-demo docker compose -f docker-compose.demo.yml up -d
```

#### Stop and clean up

```bash
docker compose -f docker-compose.demo.yml down
```

If you need to reset DB volume for a clean state:

```bash
docker compose -f docker-compose.demo.yml down -v
```

### GitHub Actions pipeline behavior

#### CI workflows (quality gates)

- Build/Test workflow for PRs.
- Coverage workflow.
- Secret scan workflow.

#### Docker Publish workflow

File: `.github/workflows/docker-publish.yml`

Triggers:

1. Push to `main`.
2. Push tags matching `v*`.
3. Manual run (`workflow_dispatch`).

Publish behavior:

1. Build Docker image from `Dockerfile`.
2. Login using repository secrets:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
3. Push tags to Docker Hub.

### Tagging strategy

1. `latest`
- Most recent successful publish from `main`.

2. `sha-<shortsha>`
- Immutable traceable image for a specific commit.

3. `vX.Y.Z` or `vX.Y.Z-demo`
- Release/demo artifact tag.

For presentations, use a fixed `v...` tag, not only `latest`.

### Release flow for demo day

1. Merge final demo commit to `main`.
2. Create and push release tag (example `v1.0.0-demo`).
3. Confirm Docker Publish workflow is green.
4. Confirm Docker Hub has the new `v1.0.0-demo` tag.
5. Pull/run that exact tag locally.
6. Teammate validates from scratch using this README only.

### Quick verification checklist

1. `docker compose -f docker-compose.demo.yml ps` shows healthy services.
2. App loads at `http://localhost:8080`.
3. Bootstrap employee/admin login works.
4. Docker Hub shows expected tags.
5. GitHub Actions publish run is green.

### Troubleshooting

#### Error: `UnknownHostException: mysql`

Cause:
- App was run alone, not on Compose network with MySQL service.

Fix:
1. Stop standalone app container.
2. Run via compose with both services:
```bash
docker compose -f docker-compose.demo.yml up -d
```

#### App starts but DB fails auth

Cause:
- `.env` credentials mismatch MySQL env values.

Fix:
1. Re-check `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD`.
2. Recreate containers if needed:
```bash
docker compose -f docker-compose.demo.yml down -v
docker compose -f docker-compose.demo.yml up -d
```

#### Port already in use

Cause:
- Another service using `8080` or `3307`.

Fix:
1. Stop conflicting service.
2. Or change host ports in compose and update run instructions.

### Team responsibilities

1. Keep image naming consistent with `lobsterchop/cookiegramproject`.
2. Keep `.env` secrets out of git.
3. Use fixed `v...` tag for demo/release.
4. Validate setup on at least one clean teammate machine before final demo.
