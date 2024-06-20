package controller.FXMLControllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
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
import model.person.Participant;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MainController class handles the primary logic for managing pairs and groups in the MainFrame of the application.
 * It manages the loading of participant lists, party locations, and executing algorithms to create pairs and groups.
 *
 * @author Daniel Hinkelmann
 */
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
    private ListView<String> pairIdentNumbersList;

    @FXML
    private TableView<Pair> pairTable;

    @FXML
    private TableColumn<Pair, String> partOneColPair;

    @FXML
    private TableColumn<Pair, String> partTwoColPair;

    @FXML
    private ListView<String> successorListPair;

    @FXML
    private TableColumn<Pair, String> kitchenColPair;

    @FXML
    private TableColumn<Pair, String> courseColPair;

    @FXML
    private MenuItem createGroups;

    @FXML
    private SplitPane pairSplitPane;

    /**
     * Opens a file chooser for selecting the participant list file.
     * Starts a monitoring thread if it's not already running.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    void openFileChooserPartList(ActionEvent event) {
        if (thread.isInterrupted()) {
            participantListPath = null;
            thread = new Thread(() -> this.monitor());
        }

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

    /**
     * Opens a file chooser for selecting the party location file.
     * Starts a monitoring thread if it's not already running.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    void openFileChooserPartLoc(ActionEvent event) {
        if (thread.isInterrupted()) {
            locationPath = null;
            thread = new Thread(() -> this.monitor());
        }

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

    /**
     * Executes the group algorithm.
     * Placeholder for the actual implementation of group creation logic.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    void executeGroupAlgo(ActionEvent event) {
        //TODO: implement
    }

    /**
     * Executes the pair algorithm by opening a new window to set pairing parameters and starting a monitoring thread.
     *
     * @param event the action event triggered by the menu item
     * @throws Exception if an error occurs while loading the FXML file
     */
    @FXML
    void executePairAlgorithm(ActionEvent event) throws Exception {
        if (pairingWeightsAcceptThread.isInterrupted()) {
            PairingWeightsController.acceptButtonFlag.flag = false;
            pairingWeightsAcceptThread = new Thread(() -> this.monitorAcceptPairingWeight());
        }

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

    /**
     * Receives the pairing weights set by the user and updates the pairlist and identnumbers.
     * Writes the pair data to the table.
     */
    private void receivePairingWeights() {
        this.pairList = new PairList(inputData, PairingWeightsController.acceptButtonFlag.pairingWeights);
        this.pairIdentNumber = this.pairList.getIdentNumber();
        writePairDataToTab();
    }

    /**
     * Writes the pair data to the table in the UI.
     * Clears existing items if necessary and sets up value factories for the table columns.
     */
    private synchronized void writePairDataToTab() {
        Platform.runLater(() -> {
            if (!pairTable.getItems().isEmpty()) {
                pairTable.getItems().clear();
            } else {
                setupValueFactories();
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(pairList.getPairs());
            pairTable.setItems(data);

            writeIdentNumbersToTab();
        });
    }

    /**
     * Sets up the value factories for the table columns to display the pair data.
     */
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

    /**
     * Writes the identifier numbers of the pairs to the list view in the UI.
     */
    private synchronized void writeIdentNumbersToTab() {
        Platform.runLater(() -> {
            if (!pairIdentNumbersList.getItems().isEmpty()) {
                pairIdentNumbersList.getItems().clear();
            }

            ObservableList<String> data = FXCollections.observableArrayList(pairIdentNumber.asList());
            pairIdentNumbersList.setItems(data);

            writeSuccessorToTab();
        });
    }

    /**
     * Writes the successors to the list view in the UI.
     */
    private synchronized void writeSuccessorToTab() {
        Platform.runLater(() -> {
            if (!successorListPair.getItems().isEmpty()) {
                successorListPair.getItems().clear();
            }

            ObservableList<String> data;
            if (pairList.getSuccessors().isEmpty()) {
                data = FXCollections.observableArrayList(List.of("Keine Nachrücker vorhanden"));
            } else {
                data = FXCollections.observableArrayList(
                        pairList
                                .getSuccessors()
                                .stream()
                                .map(Participant::toString)
                                .collect(Collectors.toList())
                );
            }
            successorListPair.setItems(data);
        });
    }

    /**
     * Monitors the paths of the participant list and location files, initializing input data once both are set.
     * Interrupts the monitoring thread once the data is initialized.
     */
    private void monitor() {
        while (participantListPath == null || locationPath == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Thread.currentThread().interrupt();
        this.inputData = InputData.getInstance(participantListPath, locationPath);
    }

    /**
     * Monitors the acceptance of pairing weights by the user, then updates the pair data in the UI.
     * Interrupts the monitoring thread once the data is updated.
     */
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
