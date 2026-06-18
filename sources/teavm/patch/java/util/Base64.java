package java.util;

public class Base64 {
    public static class Decoder {
        public byte[] decode(byte[] src) { return new byte[0]; }
        public byte[] decode(String src) { return new byte[0]; }
    }
    public static class Encoder {
        public byte[] encode(byte[] src) { return new byte[0]; }
        public String encodeToString(byte[] src) { return ""; }
    }
    public static Decoder getDecoder() { return new Decoder(); }
    public static Encoder getEncoder() { return new Encoder(); }
    public static Decoder getMimeDecoder() { return new Decoder(); }
    public static Encoder getMimeEncoder() { return new Encoder(); }
    public static Encoder getMimeEncoder(int lineLength, byte[] lineSeparator) { return new Encoder(); }
    public static Encoder getUrlEncoder() { return new Encoder(); }
    public static Decoder getUrlDecoder() { return new Decoder(); }
}
