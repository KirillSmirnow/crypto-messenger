package cryptomessenger.desktop.infrastructure.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SceneLoader {

    private static final String LOCATION_PREFIX = "/cryptomessenger/desktop/infrastructure/ui/";
    private static final String LOCATION_SUFFIX = ".fxml";

    private final ApplicationContext applicationContext;

    public Scene load(String name) {
        return load(name, SceneProperties.empty());
    }

    @SneakyThrows
    public Scene load(String name, SceneProperties sceneProperties) {
        var fxmlLocation = getClass().getResource(LOCATION_PREFIX + name + LOCATION_SUFFIX);
        var fxmlLoader = new FXMLLoader(fxmlLocation);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setResources(sceneProperties);
        return new Scene(fxmlLoader.load());
    }
}
