#!/bin/bash
# TeaVM build script that runs in the background
cd /home/z/my-project/download/eaglercraft-26.1.2

echo "Starting TeaVM build at $(date)"

# Run the build with extended timeout
./gradlew :sources:clean :sources:generateJavaScript 2>&1

echo "Build finished at $(date)"
echo "Exit code: $?"

# Check output
if [ -f sources/build/generated/teavm/js/classes.js ]; then
    SIZE=$(wc -c < sources/build/generated/teavm/js/classes.js)
    echo "classes.js size: $SIZE bytes"
else
    echo "classes.js not found!"
fi
