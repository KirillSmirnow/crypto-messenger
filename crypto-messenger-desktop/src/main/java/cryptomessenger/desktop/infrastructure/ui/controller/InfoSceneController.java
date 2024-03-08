package cryptomessenger.desktop.infrastructure.ui.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class InfoSceneController implements Initializable {

    public Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText(resources.getString("text"));
    }
}
