package javax.crypto;
import java.security.Key;
public class Cipher {
    public static final int ENCRYPT_MODE = 1;
    public static final int DECRYPT_MODE = 2;
    public static Cipher getInstance(String transformation) { return new Cipher(); }
    public void init(int opmode, Key key) {}
    public void init(int opmode, Key key, java.security.spec.AlgorithmParameterSpec params) {}
    public byte[] doFinal() { return new byte[0]; }
    public byte[] doFinal(byte[] input) { return new byte[0]; }
    public int doFinal(byte[] output, int outputOffset) { return 0; }
    public int update(byte[] input, int inputOffset, int inputLen, byte[] output) { return 0; }
}