package javax.naming;
public interface Context { Object lookup(String name) throws NamingException; }