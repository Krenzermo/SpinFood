package controller.FXMLControllers;

import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Callback;


import model.event.Course;
import model.event.Location;
import model.event.collection.Pair;
import model.event.io.InputData;
import model.event.list.PairList;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.PairingWeights;
import model.person.Name;

import java.io.File;
import java.net.URL;

public class MainController {

    private Thread thread = new Thread(() -> this.monitor());
    private Thread pairingWeightsAcceptThread = new Thread(() -> this.monitorAcceptPairingWeight());

    private InputData inputData;
    private PairList pairList;
    private IdentNumber pairIdentNumber;

    private volatile String participantListPath = null;
    private volatile String locationPath = null;

    @FXML
    private MenuItem openParticipantList;

    @FXML
    private MenuItem openPartyLoc;

    @FXML
    private MenuItem createPairs;

    @FXML
    private ListView<Double> pairIdentNumbersList;

    @FXML
    private TableView<Pair> pairTable;

    @FXML
    private TableColumn<Pair, String> partOneColPair;

    @FXML
    private TableColumn<Pair, String> partTwoColPair;

    @FXML
    private ListView<?> successorListPair;

    @FXML
    private TableColumn<Pair, String> kitchenColPair;

    @FXML
    private TableColumn<Pair, String> courseColPair;

    @FXML
    private MenuItem createGroups;

    @FXML
    void openFileChooserPartList(ActionEvent event) {

        if (!thread.isAlive()) {
            thread.start();
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Öffnen Sie die Teilnehmerliste");
        try {
            participantListPath = fileChooser.showOpenDialog(stage).getAbsolutePath();
        } catch (NullPointerException e) {
            participantListPath = null;
        }
    }

    @FXML
    void openFileChooserPartLoc(ActionEvent event) {

        if (!thread.isAlive()) {
            thread.start();
        }

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Öffnen Sie die Partylocation");
        try {
            locationPath = fileChooser.showOpenDialog(stage).getAbsolutePath();
        } catch (NullPointerException e) {
            locationPath = null;
        }
    }

    @FXML
    void executeGroupAlgo(ActionEvent event) {

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

        PairingWeightsController.stage = stage;

        pairingWeightsAcceptThread.start();

    }

    private void receivePairingWeights() {

        this.pairList = new PairList(inputData, PairingWeightsController.acceptButtonFlag.pairingWeights);
        this.pairIdentNumber = this.pairList.getIdentNumber();

        writePairDataToTab();

    }

    private void writePairDataToTab() {

        setupValueFactories();

        ObservableList<Pair> data = FXCollections.observableArrayList(pairList.getPairs());
        pairTable.setItems(data);

        writeIdentNumbersToTab();

    }



    private void setupValueFactories() {
        partOneColPair.setCellValueFactory(
                cell -> cell.getValue().getParticipants().get(0).getName().asProperty()
        );
        partTwoColPair.setCellValueFactory(
                cell -> cell.getValue().getParticipants().get(1).getName().asProperty()
        );

        kitchenColPair.setCellValueFactory(
                cell -> cell.getValue().getKitchen().asProperty()
        );

        courseColPair.setCellValueFactory(
                cell -> cell.getValue().getCourse().asProperty()
        );

    }

    private void writeIdentNumbersToTab() {
        ObservableList<Double> data = FXCollections.observableArrayList(pairIdentNumber.asList());
        pairIdentNumbersList.setItems(data);
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
    }

    private void monitorAcceptPairingWeight() {

        while (PairingWeightsController.acceptButtonFlag == null || !PairingWeightsController.acceptButtonFlag.flag) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Thread.currentThread().interrupt();
        receivePairingWeights();

    }


}
