package cryptomessenger.desktop.infrastructure.ui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

public class JavaFxApplication extends Application {

    @Setter
    private static ApplicationContext applicationContext;

    private final SceneLoader sceneLoader;
    private final String appVersion;

    public JavaFxApplication() {
        if (applicationContext == null) {
            throw new RuntimeException("Application context not provided");
        }
        sceneLoader = applicationContext.getBean(SceneLoader.class);
        appVersion = applicationContext.getEnvironment().getProperty("app.version");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CryptoMessenger — %s".formatted(appVersion));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/lock.png")));
        primaryStage.setScene(sceneLoader.load("MainScene"));
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }
}
