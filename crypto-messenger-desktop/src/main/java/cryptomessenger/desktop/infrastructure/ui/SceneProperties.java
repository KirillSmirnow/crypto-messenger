package cryptomessenger.desktop.infrastructure.ui;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Collections.emptyMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SceneProperties extends ResourceBundle {

    private final Map<String, Object> values;

    @SafeVarargs
    public static SceneProperties of(Map.Entry<String, ?>... entries) {
        return new SceneProperties(Map.ofEntries(entries));
    }

    public static SceneProperties empty() {
        return new SceneProperties(emptyMap());
    }

    @Override
    protected Object handleGetObject(String key) {
        return values.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        throw new UnsupportedOperationException();
    }
}
