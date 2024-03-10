package cryptomessenger.desktop.infrastructure.ui.controller;

import cryptomessenger.desktop.infrastructure.localstorage.LocalStorage;
import cryptomessenger.desktop.service.LocalStorageKeys;
import cryptomessenger.desktop.service.message.MessageService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@Component
@RequiredArgsConstructor
public class SendMessageSceneController implements Initializable {

    private final MessageService messageService;
    private final LocalStorage localStorage;

    public ComboBox<String> receiverSelector;
    public TextArea messageField;

    private Runnable onSent;
    private Runnable onCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receiverSelector.setItems(observableArrayList(localStorage.getStrings(LocalStorageKeys.CONTACTS)));
        var receiverUsername = resources.getString("receiverUsername");
        if (!receiverUsername.isEmpty()) {
            receiverSelector.setValue(receiverUsername);
        } else if (!receiverSelector.getItems().isEmpty()) {
            receiverSelector.setValue(receiverSelector.getItems().get(0));
        }
        onSent = (Runnable) resources.getObject("onSent");
        onCancel = (Runnable) resources.getObject("onCancel");
    }

    public void onSend(ActionEvent actionEvent) {
        messageService.send(receiverSelector.getValue(), messageField.getText());
        onSent.run();
    }

    public void onCancel(ActionEvent actionEvent) {
        onCancel.run();
    }
}
