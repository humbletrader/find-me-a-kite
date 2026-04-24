# How to Deploy the Frontend on Netlify

## Prerequisites

- A [Netlify account](https://app.netlify.com/signup) (free)
- The backend already deployed and its URL available (e.g. `https://your-backend.herokuapp.com`)

---

## Step 1 — Set the backend URL

Open `frontend/index.html` and update `window.API_BASE_URL`:

```html
<script>window.API_BASE_URL = "https://your-backend.herokuapp.com";</script>
```

---

## Step 2 — Deploy to Netlify

### Option A — Drag and drop (quickest)

1. Go to [app.netlify.com](https://app.netlify.com)
2. Log in and open the **Sites** tab
3. Drag the `frontend/` folder onto the page
4. Netlify will upload the files and give you a URL like `https://random-name.netlify.app`

### Option B — Connect a Git repository (recommended)

This option redeploys automatically every time you push to the repo.

1. Push your code to GitHub (or GitLab / Bitbucket)
2. Go to [app.netlify.com](https://app.netlify.com) → **Add new site** → **Import an existing project**
3. Connect your Git provider and select the `find-me-a-kite` repository
4. Set the build settings:

   | Setting           | Value        |
   |-------------------|--------------|
   | Base directory    | `frontend`   |
   | Build command     | *(leave empty)* |
   | Publish directory | `frontend`   |

5. Click **Deploy site**

---

## Step 3 — Set a custom domain (optional)

1. In your site dashboard go to **Domain management** → **Add a domain**
2. Enter your domain name and follow the DNS instructions Netlify provides
3. Netlify provisions an SSL certificate automatically within a few minutes

---

## Step 4 — Verify

1. Open the Netlify URL in a browser
2. Open DevTools → **Network** tab
3. Add a filter and confirm requests go to your backend URL
4. Check that results load correctly

---

## Redeploying after a change

| Change made to | What to do |
|----------------|------------|
| `frontend/` files | If using Git: push to the repo — Netlify redeploys automatically. If using drag and drop: drag the folder again. |
| Backend URL (`window.API_BASE_URL`) | Update `frontend/index.html`, then redeploy the frontend as above |
| Backend only | No frontend redeployment needed |
