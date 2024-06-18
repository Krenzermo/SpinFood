package controller.FXMLControllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import model.event.io.InputData;
import model.event.list.PairList;

import java.io.File;
import java.net.URL;

public class MainController {

    private Thread thread = new Thread(() -> this.monitor());

    private InputData inputData;

    private volatile String participantListPath = null;
    private volatile String locationPath = null;

    @FXML
    private MenuItem openParticipantList;

    @FXML
    private MenuItem openPartyLoc;

    @FXML
    private MenuItem createPairs;

    @FXML
    private ListView<?> pairIdentNumbersList;

    @FXML
    private TableView<?> pairTable;

    @FXML
    private TableColumn<?, ?> partOneColPair;

    @FXML
    private TableColumn<?, ?> partTwoColPair;

    @FXML
    private ListView<?> successorListPair;

    @FXML
    private TableColumn<?, ?> kitchenColPair;

    @FXML
    private TableColumn<?, ?> courseColPair;

    @FXML
    void openFileChooserPartList(ActionEvent event) {

        if (!thread.isAlive()) {
            thread.start();
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Öffnen Sie die Teilnehmerliste");
        participantListPath = fileChooser.showOpenDialog(stage).getAbsolutePath();
    }

    @FXML
    void openFileChooserPartLoc(ActionEvent event) {

        if (!thread.isAlive()) {
            thread.start();
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Öffnen Sie die Partylocation");
        locationPath = fileChooser.showOpenDialog(stage).getAbsolutePath();
    }

    @FXML
    void executePairAlgorithm(ActionEvent event) throws Exception{

        String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();

        URL url = new URL("file:///" + absPath);
        Parent root = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle("Paar Parameter einstellen");
        stage.setScene(new Scene(root, 369, 232));
        stage.show();

    }

    private void monitor() {
        while (participantListPath == null || locationPath == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.inputData = InputData.getInstance(participantListPath, locationPath);
        System.out.println(inputData.getParticipants());
    }

}
