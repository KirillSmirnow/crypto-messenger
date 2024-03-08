package cryptomessenger.desktop.infrastructure.ui.controller;

import cryptomessenger.desktop.service.message.MessageService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class SendMessageSceneController implements Initializable {

    private final MessageService messageService;

    public TextField receiverField;
    public TextArea messageField;

    private Runnable onSent;
    private Runnable onCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onSent = (Runnable) resources.getObject("onSent");
        onCancel = (Runnable) resources.getObject("onCancel");
    }

    public void onSend(ActionEvent actionEvent) {
        messageService.send(receiverField.getText(), messageField.getText());
        onSent.run();
    }

    public void onCancel(ActionEvent actionEvent) {
        onCancel.run();
    }
}
