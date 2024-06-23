package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MainFrame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String relPath = "src/main/java/view/fxml/source.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        URL url = new URL("file:///" + absPath);
        Parent root = FXMLLoader.load(url);
        stage.setTitle("SpinfoodPlaner");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
