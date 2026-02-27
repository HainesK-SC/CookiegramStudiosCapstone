# Developer Login Bootstrap Instructions

This project no longer stores login credentials in SQL seed files.
Development users are created at startup by `DevUserBootstrapSeeder`.

## Important

- `.env.example` **is committed** to the repository.
- Developers should **not** create or edit `.env.example`.
- Each developer should create a local `.env` file (ignored by git) by copying `.env.example`.

## 1) Create local env file

From project root:

```cmd
copy .env.example .env
```

## 2) Use `dev` profile

### IntelliJ (recommended)

In Run/Debug Configuration:

- Active profiles: `dev`

### CLI

```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

## 3) Default dev login credentials

If you did not override values in your local `.env`:

- Admin
  - Email: `dev-admin@local.test`
  - Password: `dev-admin-password`
- Employee
  - Email: `dev-employee@local.test`
  - Password: `dev-employee-password`

If you set `BOOTSTRAP_*` values in `.env`, those values are used instead.

## 4) Verify users were seeded

Open H2 console (`/h2-console`) and run:

```sql
SELECT EMAIL, ROLE FROM USERS;
```

You should see 2 rows (ADMIN + EMPLOYEE).

## Troubleshooting

- Log shows `Dev user bootstrap is disabled`:
  - You are not running with `dev` profile, or bootstrap properties are disabled.
- `USERS` table is empty:
  - App may have failed before `CommandLineRunner` executed.
  - Ensure app starts cleanly and check startup logs for `DevUserBootstrapSeeder` entries.