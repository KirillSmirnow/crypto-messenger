package cryptomessenger.desktop.infrastructure.ui;

import javafx.application.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaFxApplicationLauncher {

    private final ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    public void launch() {
        JavaFxApplication.setApplicationContext(applicationContext);
        Application.launch(JavaFxApplication.class);
    }
}
