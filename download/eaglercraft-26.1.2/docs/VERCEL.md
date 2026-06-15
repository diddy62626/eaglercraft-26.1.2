# Deploying EaglerCraftX 26.1.2 on Vercel

## What goes on Vercel vs What doesn't

| Component | Vercel? | Why |
|-----------|---------|-----|
| `index.html` | ✅ Yes | Static HTML |
| `classes.js` | ✅ Yes | Compiled TeaVM JS (static) |
| `assets.epk` | ✅ Yes | Game assets package (static) |
| `lang/` | ✅ Yes | Language files (static) |
| `favicon.png` | ✅ Yes | Static image |
| Gateway (WebSocket server) | ❌ No | Requires persistent TCP/WebSocket |
| Minecraft Server | ❌ No | Requires persistent Java process |
| BungeeCord/Velocity | ❌ No | Requires persistent Java process |

## Quick Deploy

### Option 1: Vercel CLI

```bash
# Install Vercel CLI
npm i -g vercel

# From the project root
cd eaglercraft-26.1.2

# Deploy (the public/ folder is what gets served)
vercel --prod
```

### Option 2: Connect GitHub Repo

1. Go to https://vercel.com/new
2. Import your GitHub repo: `diddy62626/eaglercraft-26.1.2`
3. Set **Root Directory** to `public/`
4. Framework: **Other**
5. Click **Deploy**

### Option 3: Drag & Drop

1. Go to https://vercel.com/new
2. Drag the `public/` folder into the deploy area

## What to put in `public/`

After compiling the client, your `public/` folder should contain:

```
public/
├── index.html          ← The HTML page (already created)
├── classes.js          ← Compiled TeaVM output (from ./build_compile.sh)
├── classes.js.map      ← Source map (optional, for debugging)
├── assets.epk          ← Compiled assets (from ./build_compile.sh)
├── lang/               ← Language files
│   ├── en_US.lang
│   └── ...
└── favicon.png         ← Your favicon
```

## Build & Deploy Script

```bash
#!/bin/bash
# build_and_deploy.sh - Compile and prepare for Vercel

set -e

# 1. Compile the client
./build_compile.sh

# 2. Copy compiled output to public/
cp output/classes.js public/
cp output/classes.js.map public/ 2>/dev/null || true
cp output/assets.epk public/
cp -r output/lang/ public/lang/

# 3. Deploy to Vercel
vercel --prod

echo "✅ Deployed!"
```

## Important: WebSocket Server

Vercel **cannot host WebSocket servers**. For multiplayer, you need:

1. **A VPS** (DigitalOcean, Linode, Hetzner, etc.) running:
   - A Minecraft Java Edition server (26.1.2)
   - EaglercraftXBungee or EaglercraftXVelocity gateway plugin

2. **Point your client** to that server's WebSocket address:
   ```js
   // In public/index.html, change the servers array:
   servers: [
       { addr: "wss://your-vps.example.com/", name: "My Server" }
   ]
   ```

3. **Enable HTTPS/WSS** on the VPS (required for browser WebSocket from Vercel's HTTPS):
   - Use nginx + Let's Encrypt as a reverse proxy
   - Or use Cloudflare Tunnel (free)

## Environment Variables (optional)

Set these in Vercel Dashboard → Settings → Environment Variables:

| Variable | Default | Description |
|----------|---------|-------------|
| None required | — | All config is in index.html's eaglercraftXOpts |

## Custom Domain

1. In Vercel Dashboard → Settings → Domains
2. Add your domain (e.g., `play.yourdomain.com`)
3. Add DNS records as instructed
4. Update your WebSocket server address to match
