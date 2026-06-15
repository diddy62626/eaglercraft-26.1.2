// ============================================================
// EaglerCraftX 26.1.2 — Build Stub
// ============================================================
// This is a placeholder. The real classes.js is produced by
// TeaVM compilation of the EaglerCraft source code against a
// real Minecraft 26.1.2 JAR.
//
// To generate the real file:
//   1. Obtain a Minecraft 26.1.2 JAR
//   2. Run: ./gradlew :sources:generateJavaScript
//   3. Copy sources/build/generated/teavm/js/classes.js here
// ============================================================

(function() {
    "use strict";

    var loadingScreen = document.getElementById("loading-screen");
    var progressBar   = document.getElementById("progress-bar");
    var progressText  = document.getElementById("progress-text");

    // Animate progress to ~80% then show the info screen
    var pct = 50;
    var targetPct = 80;
    var interval = setInterval(function() {
        if (pct < targetPct) {
            pct += 2;
            progressBar.style.width = pct + "%";
            progressText.textContent = "Loading EaglerCraftX 26.1.2...";
        } else {
            clearInterval(interval);
            showInfoScreen();
        }
    }, 40);

    function showInfoScreen() {
        progressBar.style.width = "100%";
        progressText.textContent = "Ready";

        // Replace loading screen content with info panel
        setTimeout(function() {
            loadingScreen.innerHTML = [
                '<div style="text-align:center;max-width:600px;padding:2rem;font-family:\'Segoe UI\',system-ui,sans-serif;">',

                // Title
                '<div style="font-size:2.2rem;font-weight:700;margin-bottom:0.3rem;',
                'background:linear-gradient(90deg,#00d2ff,#3a7bd5,#00d2ff);',
                'background-size:200% auto;-webkit-background-clip:text;-webkit-text-fill-color:transparent;',
                'animation:shimmer 3s linear infinite;">',
                'EaglerCraftX 26.1.2</div>',

                // Subtitle
                '<div style="font-size:1rem;opacity:0.7;margin-bottom:2rem;">',
                'Minecraft 26.1.2 in your browser — Powered by TeaVM & WebGL2</div>',

                // Status box
                '<div style="background:rgba(255,255,255,0.06);border:1px solid rgba(255,255,255,0.12);',
                'border-radius:12px;padding:1.5rem;margin-bottom:1.5rem;text-align:left;">',

                '<div style="display:flex;align-items:center;gap:0.6rem;margin-bottom:1rem;">',
                '<div style="width:10px;height:10px;border-radius:50%;background:#ffaa00;flex-shrink:0;"></div>',
                '<span style="font-size:1.05rem;font-weight:600;color:#ffcc44;">Build Stub Loaded</span>',
                '</div>',

                '<p style="font-size:0.9rem;line-height:1.6;color:rgba(224,224,224,0.85);margin:0 0 1rem 0;">',
                'The game client has not been compiled yet. You are seeing this page because ',
                '<code style="background:rgba(255,255,255,0.08);padding:0.1em 0.4em;border-radius:3px;font-size:0.85em;">classes.js</code> ',
                'is a placeholder. To get a playable client, the EaglerCraft Java source must be ',
                'compiled through TeaVM against a real Minecraft 26.1.2 JAR.</p>',

                '<p style="font-size:0.85rem;line-height:1.5;color:rgba(224,224,224,0.6);margin:0;">',
                'MC 26.1.2 is the first unobfuscated Minecraft release (protocol 775, data version 4790). ',
                'No MCP decompiler is needed — the source maps directly to the JAR.</p>',
                '</div>',

                // Steps
                '<div style="background:rgba(255,255,255,0.04);border:1px solid rgba(255,255,255,0.08);',
                'border-radius:12px;padding:1.5rem;margin-bottom:1.5rem;text-align:left;">',

                '<div style="font-size:0.95rem;font-weight:600;margin-bottom:1rem;color:#00d2ff;">',
                'How to compile the full client</div>',

                '<ol style="font-size:0.88rem;line-height:1.8;color:rgba(224,224,224,0.8);margin:0;padding-left:1.4rem;">',
                '<li>Obtain a <strong style="color:#e0e0e0;">Minecraft 26.1.2</strong> JAR file</li>',
                '<li>Place it in the project root as <code style="background:rgba(255,255,255,0.08);padding:0.1em 0.4em;border-radius:3px;font-size:0.85em;">minecraft-26.1.2.jar</code></li>',
                '<li>Run <code style="background:rgba(255,255,255,0.08);padding:0.1em 0.4em;border-radius:3px;font-size:0.85em;">./gradlew :sources:generateJavaScript</code></li>',
                '<li>Copy the output to <code style="background:rgba(255,255,255,0.08);padding:0.1em 0.4em;border-radius:3px;font-size:0.85em;">public/classes.js</code></li>',
                '<li>Commit and push — GitHub Actions will deploy automatically</li>',
                '</ol></div>',

                // Tech specs
                '<div style="display:flex;flex-wrap:wrap;gap:0.6rem;justify-content:center;margin-bottom:1.5rem;">',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">Protocol 775</span>',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">TeaVM 0.15.0</span>',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">WebGL2</span>',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">Java 25</span>',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">Gradle 9.5.1</span>',
                '<span style="background:rgba(0,210,255,0.12);border:1px solid rgba(0,210,255,0.25);color:#00d2ff;',
                'padding:0.3em 0.8em;border-radius:20px;font-size:0.78rem;">Unobfuscated</span>',
                '</div>',

                // Footer
                '<div style="font-size:0.78rem;opacity:0.35;">',
                'EaglerCraftX 26.1.2 &bull; GitHub: diddy62626/eaglercraft-26.1.2</div>',

                '</div>'
            ].join('');
        }, 400);
    }

    // Log warning
    console.warn("[EaglerCraftX 26.1.2] Build stub loaded — not a playable client.");
    console.warn("[EaglerCraftX 26.1.2] To compile the real client, run: ./gradlew :sources:generateJavaScript");

})();
