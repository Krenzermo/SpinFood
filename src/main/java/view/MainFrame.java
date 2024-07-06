package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

/**
 * Run with this VM configuration:
 * <p>
 * --module-path
 * "C:\Program Files\Java\javafx-sdk-22.0.1\lib"
 * --add-modules
 * javafx.controls,javafx.fxml
 */
public class MainFrame extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainFrame.stage = stage;

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
