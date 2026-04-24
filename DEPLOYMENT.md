# Deployment Guide

## Backend (Spring Boot)

1. Set the following environment variables on your hosting platform (e.g. Heroku):

   | Variable             | Description                                      | Example                              |
   |----------------------|--------------------------------------------------|--------------------------------------|
   | `JDBC_DATABASE_URL`  | PostgreSQL connection URL                        | `jdbc:postgresql://host:5432/dbname` |
   | `JDBC_DATABASE_USERNAME` | Database username                            | `kitefinder`                         |
   | `JDBC_DATABASE_PASSWORD` | Database password                            | `secret`                             |
   | `SUPPORTER_TOKENS`   | Comma-separated list of valid supporter tokens   | `token1,token2,token3`               |
   | `FRONTEND_URL`       | Origin of the deployed frontend (used for CORS)  | `https://find-me-a-kite.netlify.app` |
   | `PORT`               | Port the server listens on (default: `5000`)     | `5000`                               |

2. Build and deploy the JAR:
   ```bash
   mvn clean package
   java -jar target/findmeakite-1.12.0.jar
   ```

---

## Frontend (static files in `frontend/`)

1. Open `frontend/index.html` and set `window.API_BASE_URL` to the URL of your deployed backend:

   ```html
   <!-- Before you deploy: set this to your Spring Boot backend URL -->
   <script>window.API_BASE_URL = "https://your-backend.herokuapp.com";</script>
   ```

   Leave it as `""` only if the frontend and backend are served from the same domain.

2. Deploy the `frontend/` directory to any static hosting provider:

   | Provider      | How to deploy                                                  |
   |---------------|----------------------------------------------------------------|
   | **Netlify**   | Drag and drop the `frontend/` folder on netlify.com, or connect the repo — `netlify.toml` is already configured |
   | **Vercel**    | `vercel --cwd frontend`                                        |
   | **GitHub Pages** | Push `frontend/` contents to a `gh-pages` branch           |
   | **Nginx**     | Copy `frontend/` to `/var/www/html` and serve with nginx       |

---

## Verify the connection

After deploying both parts, open the frontend in a browser and check that:

- The page loads without console errors
- Selecting a filter triggers a network request to your backend URL (visible in browser DevTools → Network tab)
- Results are returned correctly
