package controller.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.event.io.InputData;

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
    void executePairAlgorithm(ActionEvent event) {

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
