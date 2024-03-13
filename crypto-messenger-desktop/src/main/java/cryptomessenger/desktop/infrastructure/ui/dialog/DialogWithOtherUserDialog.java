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
public class DialogWithOtherUserDialog {

    private final SceneLoader sceneLoader;

    public void show(String username) {
        var stage = new Stage();
        stage.setTitle("Dialog with " + username);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/lock.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        var sceneProperties = SceneProperties.of(
                entry("otherUsername", username == null ? "" : username)
        );
        stage.setScene(sceneLoader.load("DialogScene", sceneProperties));
        stage.show();
    }
}
