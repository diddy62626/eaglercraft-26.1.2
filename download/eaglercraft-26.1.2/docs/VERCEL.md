# Deploying EaglerCraftX 26.1.2 on Vercel

## How It Works

The GitHub Actions workflow (`.github/workflows/build-deploy.yml`) automatically:
1. Builds the client with Java 25 + Node.js 22
2. Compiles TeaVM (Java → JavaScript)
3. Assembles everything into `public/`
4. Deploys to both **GitHub Pages** and **Vercel**

## One-Time Setup

### 1. Enable GitHub Pages
1. Go to your repo → **Settings** → **Pages**
2. Source → **Deploy from a branch**
3. Branch → **gh-pages** → `/ (root)`
4. Save

### 2. Add Vercel Secrets (optional but recommended)

Go to your repo → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

| Secret | How to get it |
|--------|---------------|
| `VERCEL_TOKEN` | Go to https://vercel.com/account/tokens → Create Token |
| `VERCEL_ORG_ID` | Go to https://vercel.com/account → copy your ID |
| `VERCEL_PROJECT_ID` | Run `vercel link` locally, then check `.vercel/project.json` |

### 3. Add Minecraft JAR (required for full build)

The MC 26.1.2 JAR can't be in the repo (copyright). Options:

**Option A: Upload as workflow artifact** (recommended)
1. Go to **Actions** → **Build & Deploy** → **Run workflow**
2. Paste a URL to the MC 26.1.2 JAR in the `minecraft_jar_url` field
3. Run

**Option B: Base64 in a secret**
```bash
# Encode the JAR
base64 -w0 minecraft-26.1.2.jar > mcjar.b64

# Add as GitHub secret: MINECRAFT_JAR_B64
```

Then the workflow decodes it.

## What Vercel Serves

```
public/
├── index.html          ← Game client page
├── classes.js          ← Compiled game engine (from TeaVM)
├── assets.epk          ← Game assets
├── lang/               ← Language files
└── favicon.png         ← Favicon
```

## Auto-Deploy Flow

```
Push to main → GitHub Actions builds → Deploys to:
  ├── GitHub Pages (gh-pages branch)
  └── Vercel (if secrets configured)
```

## Manual Deploy (without GitHub Actions)

```bash
npm i -g vercel

# Build locally first
./build_compile.sh

# Copy output to public/
cp output/classes.js public/
cp output/assets.epk public/
cp -r output/lang/ public/lang/

# Deploy
vercel --prod
```

## ⚠️ WebSocket Server

Vercel **cannot host game servers**. For multiplayer you need a separate VPS running the gateway plugin. Update `public/index.html`:

```js
servers: [
    { addr: "wss://your-vps.example.com/", name: "My Server" }
]
```
