package cryptomessenger.desktop.infrastructure.ui.dialog;

import cryptomessenger.desktop.infrastructure.ui.SceneLoader;
import cryptomessenger.desktop.infrastructure.ui.SceneProperties;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
public class ErrorDialog {

    private final SceneLoader sceneLoader;

    public void show(String text) {
        var stage = new Stage();
        stage.setTitle("Error");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/error.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        var sceneProperties = SceneProperties.of(entry("text", text));
        stage.setScene(sceneLoader.load("InfoScene", sceneProperties));
        stage.show();
    }
}
