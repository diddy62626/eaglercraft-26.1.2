package java.math;
public class BigInteger extends Number {
    public static final BigInteger ZERO = new BigInteger(new byte[0]);
    public static final BigInteger ONE = new BigInteger(new byte[]{1});
    public BigInteger(byte[] val) {}
    public BigInteger(int numBits, java.util.Random rnd) {}
    public BigInteger(String val) {}
    public BigInteger add(BigInteger val) { return this; }
    public BigInteger subtract(BigInteger val) { return this; }
    public BigInteger multiply(BigInteger val) { return this; }
    public BigInteger divide(BigInteger val) { return this; }
    public BigInteger mod(BigInteger m) { return this; }
    public BigInteger modPow(BigInteger exponent, BigInteger m) { return this; }
    public BigInteger abs() { return this; }
    public BigInteger negate() { return this; }
    public int compareTo(BigInteger val) { return 0; }
    public byte[] toByteArray() { return new byte[0]; }
    public int intValue() { return 0; }
    public long longValue() { return 0L; }
    public float floatValue() { return 0f; }
    public double doubleValue() { return 0.0; }
    public String toString() { return "0"; }
}
