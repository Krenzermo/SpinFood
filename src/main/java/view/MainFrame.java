package view;

import controller.FXMLControllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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

        FXMLLoader loader = new FXMLLoader(url);
        stage.setScene(new Scene(loader.load()));

        stage.setTitle("SpinfoodPlaner");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
