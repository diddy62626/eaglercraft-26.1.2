package java.net;
public abstract class Authenticator {
    public static void setDefault(Authenticator a) {}
    protected abstract PasswordAuthentication requestPasswordAuthenticationInstance();
}