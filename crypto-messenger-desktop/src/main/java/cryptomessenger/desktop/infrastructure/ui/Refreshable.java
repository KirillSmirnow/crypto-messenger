package cryptomessenger.desktop.infrastructure.ui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public interface Refreshable extends Initializable {

    void refresh();

    @Override
    default void initialize(URL location, ResourceBundle resources) {
        refresh();
    }
}
