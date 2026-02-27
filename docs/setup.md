# Setup Guide (Team)

## Do I need to create `.env.example`?

No.

- `.env.example` is tracked in git and will be pulled automatically.
- Each developer creates their own local `.env` from it.

## Quick Start

1. Copy template:

```cmd
copy .env.example .env
```

2. (Optional) Edit `.env` values for your own local credentials.

3. Run app with dev profile:

```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

4. Login with either:

- Values from your `.env` (`BOOTSTRAP_ADMIN_*`, `BOOTSTRAP_EMPLOYEE_*`), or
- Defaults from `application-dev.properties` if you did not override them:
  - `dev-admin@local.test` / `dev-admin-password`
  - `dev-employee@local.test` / `dev-employee-password`

## Verify Bootstrap

In H2 console (`/h2-console`):

```sql
SELECT EMAIL, ROLE FROM USERS;
```

Expected: seeded ADMIN and EMPLOYEE users.