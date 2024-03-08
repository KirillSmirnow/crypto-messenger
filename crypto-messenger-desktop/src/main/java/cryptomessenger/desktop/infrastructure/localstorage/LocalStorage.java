package cryptomessenger.desktop.infrastructure.localstorage;

public interface LocalStorage {

    void save(String key, byte[] value);

    default void save(String key, String value) {
        save(key, value.getBytes());
    }

    byte[] getBytes(String key);

    default String getString(String key) {
        return new String(getBytes(key));
    }
}
