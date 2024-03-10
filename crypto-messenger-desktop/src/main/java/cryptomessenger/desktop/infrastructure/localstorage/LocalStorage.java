package cryptomessenger.desktop.infrastructure.localstorage;

import java.util.Arrays;
import java.util.List;

public interface LocalStorage {

    void save(String key, byte[] value);

    default void save(String key, String value) {
        save(key, value.getBytes());
    }

    default void save(String key, List<String> values) {
        save(key, String.join(";", values));
    }

    byte[] getBytes(String key);

    default String getString(String key) {
        return new String(getBytes(key));
    }

    default List<String> getStrings(String key) {
        return Arrays.asList(getString(key).split(";"));
    }
}
