# How to Run the App Locally

The app has two parts that run independently: the **backend** (Spring Boot on port 8081) and the **frontend** (static files served on port 3000).

## Prerequisites

| Tool | Version | Check |
|------|---------|-------|
| Java | 21 | `java -version` |
| Maven | 3.x | `mvn -version` |
| PostgreSQL | any recent | `psql --version` |
| A static file server | e.g. `npx serve` | `node -v` |

---

## Step 1 — Set up PostgreSQL

Start a PostgreSQL instance on port **5433** (as expected by `application-dev.yml`) and create the required user and database:

```sql
CREATE USER kitefinder WITH PASSWORD 'cezmeulmeuvrei';
CREATE DATABASE postgres;
GRANT ALL PRIVILEGES ON DATABASE postgres TO kitefinder;
```

> If your PostgreSQL runs on the default port 5432, update the URL in `src/main/resources/application-dev.yml`:
> ```yaml
> url: "jdbc:postgresql://localhost:5432/postgres"
> ```

---

## Step 2 — Run the backend

The dev profile (`application-dev.yml`) is used automatically when no other profile is active.

```bash
mvn spring-boot:run
```

The backend starts on **http://localhost:8081**.

You can verify it is up by checking the health endpoint:

```bash
curl http://localhost:8081/actuator/health
```

> **Supporter tokens for local testing:** `test1`, `test2`, `test3` (configured in `application-dev.yml`)

---

## Step 3 — Serve the frontend

The frontend is plain static files — no build step needed. Serve the `frontend/` directory with any static file server on port **3000**.

Using `npx serve` (Node.js required):

```bash
npx serve frontend -l 3000
```

Or using Python:

```bash
python3 -m http.server 3000 --directory frontend
```

The frontend is now available at **http://localhost:3000**.

---

## Step 4 — Verify

| URL | What you should see |
|-----|---------------------|
| http://localhost:3000 | The search page |
| http://localhost:3000/notifications.html | The notification management page |
| http://localhost:8081/actuator/health | `{"status":"UP"}` |

Open browser DevTools → **Network** tab and confirm that search requests go to `http://localhost:8081`.

> **Note:** `window.API_BASE_URL` in `frontend/index.html` (and `notifications.html`) is set to `""` by default, which means the frontend calls the backend at the **same origin**. Since the frontend is on port 3000 and the backend on 8081 this will fail — set it to the backend URL:
>
> ```html
> <script>window.API_BASE_URL = "http://localhost:8081";</script>
> ```
>
> CORS is already configured in the backend to allow `http://localhost:3000`.

---

## Stopping the services

| Service | How to stop |
|---------|-------------|
| Backend | `Ctrl+C` in the Maven terminal |
| Frontend | `Ctrl+C` in the serve terminal |
