package java.net;

import java.io.IOException;
import java.util.jar.JarFile;

/**
 * TeaVM stub for java.net.JarURLConnection.
 *
 * Browser environment cannot read JAR files directly; this stub
 * satisfies TeaVM compilation for log4j's ResolverUtil which
 * references this class. All operations return empty/null defaults.
 */
public abstract class JarURLConnection extends URLConnection {

    protected JarURLConnection(URL url) throws IOException {
        super(url);
    }

    public abstract JarFile getJarFile() throws IOException;

    public java.util.jar.JarEntry getJarEntry() throws IOException {
        return null;
    }

    public java.util.jar.Attributes getAttributes() throws IOException {
        return null;
    }

    public java.util.jar.Manifest getManifest() throws IOException {
        return null;
    }

    public java.security.cert.Certificate[] getCertificates() {
        return null;
    }

    public URL getJarFileURL() {
        return url;
    }

    public String getEntryName() {
        return null;
    }

    public java.util.jar.Attributes getMainAttributes() throws IOException {
        return null;
    }
}
