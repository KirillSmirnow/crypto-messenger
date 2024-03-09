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
public class SendMessageDialog {

    private final SceneLoader sceneLoader;

    public void show(Runnable onSuccess) {
        var stage = new Stage();
        stage.setTitle("Send Message");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/lock.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        var sceneProperties = SceneProperties.of(
                entry("onSent", (Runnable) () -> {
                    stage.close();
                    onSuccess.run();
                }),
                entry("onCancel", (Runnable) stage::close)
        );
        stage.setScene(sceneLoader.load("SendMessageScene", sceneProperties));
        stage.show();
    }
}
