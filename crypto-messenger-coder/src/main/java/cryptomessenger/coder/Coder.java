package cryptomessenger.coder;

public interface Coder {

    byte[] encode(byte[] key, byte[] content);

    byte[] decode(byte[] key, byte[] content);
}
