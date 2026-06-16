package javax.naming.directory;
public class InitialDirContext implements DirContext {
    public InitialDirContext() {}
    public InitialDirContext(java.util.Hashtable<?,?> environment) {}
    public Object lookup(String name) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<javax.naming.NameClassPair> list(String name) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<javax.naming.Binding> listBindings(String name) throws javax.naming.NamingException { return null; }
    public void bind(String name, Object obj) throws javax.naming.NamingException {}
    public void rebind(String name, Object obj) throws javax.naming.NamingException {}
    public void unbind(String name) throws javax.naming.NamingException {}
    public void rename(String oldName, String newName) throws javax.naming.NamingException {}
    public Object lookupLink(String name) throws javax.naming.NamingException { return null; }
    public String getNameInNamespace() throws javax.naming.NamingException { return ""; }
    public void close() throws javax.naming.NamingException {}
    public Attribute get(String name, String attrID) throws javax.naming.NamingException { return null; }
    public Attributes getAttributes(String name) throws javax.naming.NamingException { return null; }
    public Attributes getAttributes(String name, String[] attrIds) throws javax.naming.NamingException { return null; }
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws javax.naming.NamingException {}
    public void modifyAttributes(String name, javax.naming.directory.ModificationItem[] mods) throws javax.naming.NamingException {}
    public javax.naming.Context createSubcontext(String name) throws javax.naming.NamingException { return null; }
    public javax.naming.Context createSubcontext(String name, Attributes attrs) throws javax.naming.NamingException { return null; }
    public Attributes getSchema(String name) throws javax.naming.NamingException { return null; }
    public Attributes getSchemaClassDefinition(String name) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes, String[] attributesToReturn) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<SearchResult> search(String name, String filter, java.util.Hashtable<?,?> environment) throws javax.naming.NamingException { return null; }
    public javax.naming.NamingEnumeration<SearchResult> search(String name, String filterExpr, Object[] filterArgs, java.util.Hashtable<?,?> environment) throws javax.naming.NamingException { return null; }
}
