package java.nio.charset;
public abstract class CharsetDecoder {
    public final Charset charset() { return null; }
    public final CharsetDecoder onMalformedInput(CodingErrorAction newAction) { return this; }
    public final CharsetDecoder onUnmappableCharacter(CodingErrorAction newAction) { return this; }
}