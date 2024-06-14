package controller.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    private Button button;

    @FXML
    private void initialize() {
        button.setOnAction(event -> System.out.println("Button clicked!"));
    }
}
