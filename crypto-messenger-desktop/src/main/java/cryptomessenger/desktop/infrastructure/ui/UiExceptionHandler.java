package cryptomessenger.desktop.infrastructure.ui;

import cryptomessenger.desktop.infrastructure.ui.dialog.ErrorDialog;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UiExceptionHandler {

    private final ErrorDialog errorDialog;

    @AfterThrowing(pointcut = "@within(org.springframework.stereotype.Service)", throwing = "exception")
    public void handle(Exception exception) {
        errorDialog.show(exception.getMessage());
    }
}
