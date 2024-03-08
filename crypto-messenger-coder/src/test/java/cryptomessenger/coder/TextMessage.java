package cryptomessenger.coder;

import lombok.Data;

@Data
public class TextMessage implements Message {

    private final String text;

    @Override
    public byte[] getBytes() {
        return text.getBytes();
    }
}
