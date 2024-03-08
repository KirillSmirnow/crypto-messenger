package cryptomessenger.coder;

public interface Message {

    byte[] getBytes();

    default String getText() {
        return new String(getBytes());
    }
}
