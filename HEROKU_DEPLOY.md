# How to Deploy the Backend on Heroku

## Prerequisites

- A [Heroku account](https://signup.heroku.com) (free tier available)
- [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) installed
- [Git](https://git-scm.com) installed
- A PostgreSQL database (Heroku provides one as an add-on — see Step 3)
- Maven installed locally (`mvn -version` to check)

---

## Step 1 — Log in to Heroku

```bash
heroku login
```

This opens a browser window to complete authentication.

---

## Step 2 — Create a Heroku app

```bash
heroku create your-app-name
```

Replace `your-app-name` with a name of your choice. Heroku will assign a URL like `https://your-app-name.herokuapp.com`.

---

## Step 3 — Provision a PostgreSQL database

```bash
heroku addons:create heroku-postgresql:essential-0 --app your-app-name
```

This automatically sets the `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME`, and `JDBC_DATABASE_PASSWORD` environment variables on the app.

> **Note:** The `essential-0` plan is the cheapest paid PostgreSQL plan on Heroku (~$5/month). Heroku removed the free PostgreSQL tier in 2022.

Verify the variables were set:

```bash
heroku config --app your-app-name
```

---

## Step 4 — Set the remaining environment variables

```bash
heroku config:set SUPPORTER_TOKENS=token1,token2,token3 --app your-app-name
heroku config:set FRONTEND_URL=https://your-frontend.netlify.app --app your-app-name
```

| Variable            | Description                                         |
|---------------------|-----------------------------------------------------|
| `SUPPORTER_TOKENS`  | Comma-separated list of valid supporter tokens      |
| `FRONTEND_URL`      | URL of the deployed frontend (used for CORS)        |
| `PORT`              | Set automatically by Heroku — do not set manually   |

---

## Step 5 — Configure Maven to resolve the private dependency

The project depends on `findmeakite-sql-builder` hosted on GitHub Packages. Heroku needs credentials to download it during the build.

Add your GitHub username and a [personal access token](https://github.com/settings/tokens) (with `read:packages` scope) to the Heroku build environment:

```bash
heroku config:set GITHUB_ACTOR=your-github-username --app your-app-name
heroku config:set GITHUB_TOKEN=your-personal-access-token --app your-app-name
```

Then make sure `settings.xml` at the project root references these variables:

```xml
<server>
  <id>github</id>
  <username>${env.GITHUB_ACTOR}</username>
  <password>${env.GITHUB_TOKEN}</password>
</server>
```

---

## Step 6 — Deploy

Heroku deploys from Git. Push the branch you want to deploy:

```bash
git push heroku main
```

Or if you are on a different branch:

```bash
git push heroku feature/change_ui_to_react:main
```

Heroku will:
1. Detect the Maven project
2. Run `mvn clean package -DskipTests`
3. Start the app using the `Procfile`: `java -jar target/findmeakite-1.12.0.jar`

---

## Step 7 — Verify

```bash
heroku open --app your-app-name
```

Or check the logs if something goes wrong:

```bash
heroku logs --tail --app your-app-name
```

---

## Redeploying after a change

```bash
git push heroku main
```

Heroku rebuilds and restarts the app automatically on every push.

---

## Quick reference

| Task                        | Command                                          |
|-----------------------------|--------------------------------------------------|
| View logs                   | `heroku logs --tail --app your-app-name`         |
| Restart the app             | `heroku restart --app your-app-name`             |
| View all config vars        | `heroku config --app your-app-name`              |
| Open a database console     | `heroku pg:psql --app your-app-name`             |
| Check running dynos         | `heroku ps --app your-app-name`                  |
