package javax.xml.transform;
public abstract class Transformer { public abstract void transform(Source xmlSource, Result outputTarget) throws TransformerException; }