package controller.FXMLControllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import model.event.Course;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.io.InputData;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;
import view.MainFrame;

import java.io.File;

/**
 * MainController class handles the primary logic for managing pairs and groups in the MainFrame of the application.
 * It manages the loading of participant lists, party locations, and executing algorithms to create pairs and groups.
 *
 * @author Daniel Hinkelmann
 */
public class MainController {
    private final InputData inputData = InputData.getInstance();
    private PairList pairList;
    private IdentNumber pairIdentNumber;
    private GroupList groupList;
    private IdentNumber groupIdentNumber;

    private volatile String participantListPath = null;
    private volatile String locationPath = null;

    @FXML
    private VBox root;

    @FXML
    private MenuItem openParticipantList;

    @FXML
    private MenuItem openPartyLoc;

    @FXML
    private MenuItem createPairs;

    @FXML
    private ListView<String> pairIdentNumbersList;

    @FXML
    private ListView<String> groupIdentNumbersList;

    @FXML
    private TableView<Pair> pairTable;

    @FXML
    private TableColumn<Pair, Integer> idColPair;

    @FXML
    private TableColumn<Pair, String> partOneColPair;

    @FXML
    private TableColumn<Pair, String> partTwoColPair;

    @FXML
    private TableColumn<Pair, String> genderOneColPair;

    @FXML
    private TableColumn<Pair, String> genderTwoColPair;

    @FXML
    private TableColumn<Pair, String> kitchenColPair;

    @FXML
    private TableColumn<Pair, String> courseColPair;

    @FXML
    private TableColumn<Pair, String> foodTypeColPair;

    @FXML
    private TableColumn<Pair, Boolean> signedUpTogetherColPair;

    @FXML
    private TableView<Participant> successorsPairList;

    @FXML
    private TableColumn<Participant, String> idParticipantSuccessors;

    @FXML
    private TableColumn<Participant, String> nameParticipantSuccessors;

    @FXML
    private TableColumn<Participant, String> genderParticipantSuccessors;

    @FXML
    private TableColumn<Participant, String> hasKitchenParticipantSuccessors;

    @FXML
    private TableColumn<Participant, String> kitchenParticipantSuccessors;

    @FXML
    private TableColumn<Participant, String> foodTypeParticipantSuccessors;

    @FXML
    private MenuItem createGroups;

    @FXML
    private SplitPane pairSplitPane;

    @FXML
    private TableView<Group> groupTable;

    @FXML
    private TableColumn<Group, Integer> idColGroup;

    @FXML
    private TableColumn<Group, Integer> pairOneColGroup;

    @FXML
    private TableColumn<Group, Integer> pairTwoColGroup;

    @FXML
    private TableColumn<Group, Integer> pairThreeColGroup;

    @FXML
    private TableColumn<Group, String> kitchenColGroup;

    @FXML
    private TableColumn<Group, String> courseColGroup;

    @FXML
    private TableView<Pair> successorsGroupList;

    @FXML
    private TableColumn<Pair, Integer> idColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> partOneColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> partTwoColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> genderOneColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> genderTwoColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> kitchenColPairSuccessors;

    @FXML
    private TableColumn<Pair, String> foodTypeColPairSuccessors;

    @FXML
    private TableColumn<Pair, Boolean> signedUpTogetherColPairSuccessors;


    @FXML
    private MenuItem comparePairList;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab pairTab;

    @FXML
    private Tab groupTab;

    @FXML
    private Button splitPairButton;

    @FXML
    private Button createPairButton;

    @FXML
    private Button splitGroupButton;

    @FXML
    private Button createGroupButton;

    //private Stage primaryStage = (Stage) root.getScene().getWindow();

    @FXML
    public void initialize() {
        // this does not work 100%, but I haven't found a suitable property to attach the listener to
        // this works, but you may have to resize the window after hiding/showing columns.
        pairTable.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        pairTable.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        partOneColPair.setReorderable(false);
        partTwoColPair.setReorderable(false);
        genderOneColPair.setReorderable(false);
        genderTwoColPair.setReorderable(false);
        kitchenColPair.setReorderable(false);
        courseColPair.setReorderable(false);
        foodTypeColPair.setReorderable(false);
        signedUpTogetherColPair.setReorderable(false);

        groupTable.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        groupTable.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        pairOneColGroup.setReorderable(false);
        pairTwoColGroup.setReorderable(false);
        pairThreeColGroup.setReorderable(false);
        kitchenColGroup.setReorderable(false);
        courseColGroup.setReorderable(false);

        successorsPairList.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(successorsPairList));
        successorsPairList.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(successorsPairList));
        idParticipantSuccessors.setReorderable(false);
        nameParticipantSuccessors.setReorderable(false);
        genderParticipantSuccessors.setReorderable(false);
        hasKitchenParticipantSuccessors.setReorderable(false);
        kitchenParticipantSuccessors.setReorderable(false);
        foodTypeParticipantSuccessors.setReorderable(false);

        successorsGroupList.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(successorsGroupList));
        successorsGroupList.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(successorsGroupList));
        idColPairSuccessors.setReorderable(false);
        partOneColPairSuccessors.setReorderable(false);
        partTwoColPairSuccessors.setReorderable(false);
        genderOneColPairSuccessors.setReorderable(false);
        genderTwoColPairSuccessors.setReorderable(false);
        kitchenColPairSuccessors.setReorderable(false);
        foodTypeColPairSuccessors.setReorderable(false);
        signedUpTogetherColPairSuccessors.setReorderable(false);

        pairTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        groupTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        successorsPairList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        successorsGroupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // copy contained in PairListComparisonController
    private static <E> void adjustColumnWidths(TableView<E> tableView) {
        long visibleColumns = tableView.getColumns().stream().filter(TableColumn::isVisible).count();
        if (visibleColumns > 0) {
            double newWidth = tableView.getWidth() / visibleColumns;
            for (TableColumn<E, ?> column : tableView.getColumns()) {
                if (column.isVisible()) {
                    column.setPrefWidth(newWidth);
                    //column.setMinWidth(newWidth);
                    //column.setMaxWidth(newWidth);
                }
            }
        }
    }

    /**
     * Opens a file chooser for selecting the participant list file.
     * Starts a monitoring thread if it's not already running.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    void openFileChooserPartList(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter csvFilter = new ExtensionFilter("CSV-Dateien (*.csv)", "*.csv");
        ExtensionFilter txtFilter = new ExtensionFilter("Textdateien (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().addAll(csvFilter, txtFilter);
        fileChooser.setTitle("Öffnen Sie die Teilnehmerliste");
        try {
            participantListPath = fileChooser.showOpenDialog(root.getScene().getWindow()).getAbsolutePath();
            inputData.initParticipants(participantListPath);
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
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter csvFilter = new ExtensionFilter("CSV-Dateien (*.csv)", "*.csv");
        ExtensionFilter txtFilter = new ExtensionFilter("Textdateien (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().addAll(csvFilter, txtFilter);
        fileChooser.setTitle("Öffnen Sie die Partylocation");
        try {
            locationPath = fileChooser.showOpenDialog(root.getScene().getWindow()).getAbsolutePath();
            inputData.initEventLocation(locationPath);
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
        String relPath = "src/main/java/view/fxml/groupWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        GroupWeightsController dialog = new GroupWeightsController();
        dialog.init(root.getScene().getWindow());

        GroupWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.groupList = new GroupList(pairList, weights);
                this.groupIdentNumber = this.groupList.getIdentNumber();

                writeGroupDataToTab();
                writeGroupListIdentNumbersToTab();
                writeGroupListSuccessorsToTab();

                tabPane.getSelectionModel().select(groupTab);
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
        }
        event.consume();
    }

    /**
     * Executes the pair algorithm by opening a new window to set pairing parameters and starting a monitoring thread.
     *
     * @param event the action event triggered by the menu item
     * @throws Exception if an error occurs while loading the FXML file
     */
    @FXML
    void executePairAlgorithm(ActionEvent event) throws Exception {
        String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        PairingWeightsController dialog = new PairingWeightsController();
        dialog.init(root.getScene().getWindow());

        PairingWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.pairList = new PairList(weights);
                this.pairIdentNumber = this.pairList.getIdentNumber();

                if (groupList != null) {
                    groupList.clear();
                    groupTable.getItems().clear();
                    successorsGroupList.getItems().clear();
                    groupIdentNumber = null;
                    groupIdentNumbersList.getItems().clear();
                }

                writePairDataToTab();
                writePairListIdentNumbersToTab();
                writePairListSuccessorsToTab();

                tabPane.getSelectionModel().select(pairTab);
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
        }
        event.consume();
    }


    /**
     * Writes the pair data to the table in the UI.
     * Clears existing items if necessary and sets up value factories for the table columns.
     */
    protected synchronized void writePairDataToTab() {
        Platform.runLater(() -> {
            if (!pairTable.getItems().isEmpty()) {
                pairTable.getItems().clear();
            } else {
                setupPairListValueFactories();
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(pairList.getPairs());
            pairTable.setItems(data);
        });
    }

    protected synchronized void writeGroupDataToTab() {
        Platform.runLater(() -> {
            if (!groupTable.getItems().isEmpty()) {
                groupTable.getItems().clear();
            } else {
                setupGroupListValueFactories();
            }

            ObservableList<Group> data = FXCollections.observableArrayList(groupList.getGroups());
            groupTable.setItems(data);
        });
    }

    /**
     * Sets up the value factories for the table columns to display the pair data.
     */
    protected void setupPairListValueFactories() {
        idColPair.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        partOneColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getName().asObservable());
        partTwoColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getName().asObservable());
        genderOneColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getGender().asObservable());
        genderTwoColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getGender().asObservable());
        kitchenColPair.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        courseColPair.setCellValueFactory(
                cell -> {
                    Course course = cell.getValue().getCourse();
                    if (course == null) {
                        return new SimpleStringProperty("n.V.");
                    }
                    return course.asProperty();
                }
        );
        foodTypeColPair.setCellValueFactory(cell -> cell.getValue().getFoodType().asObservable());
        signedUpTogetherColPair.setCellValueFactory(cell -> cell.getValue().getSignedUpTogetherAsObservable());
    }

    protected void setupGroupListValueFactories() {
        idColGroup.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        pairOneColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[0].getIdAsObservable());
        pairTwoColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[1].getIdAsObservable());
        pairThreeColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[2].getIdAsObservable());
        kitchenColGroup.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        courseColGroup.setCellValueFactory(cell -> cell.getValue().getCourse().asProperty());
    }

    private void setupParticipantSuccessorsValueFactories() {
        idParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        nameParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().getName().asObservable());
        genderParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().getGender().asObservable());
        hasKitchenParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().isHasKitchen().asObservable());
        kitchenParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        foodTypeParticipantSuccessors.setCellValueFactory(cell -> cell.getValue().getFoodType().asObservable());
    }

    private void setupPairSuccessorsValueFactories() {
        idColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        partOneColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getName().asObservable());
        partTwoColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getName().asObservable());
        genderOneColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getGender().asObservable());
        genderTwoColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getGender().asObservable());
        kitchenColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        foodTypeColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getFoodType().asObservable());
        signedUpTogetherColPairSuccessors.setCellValueFactory(cell -> cell.getValue().getSignedUpTogetherAsObservable());
    }

    /**
     * Writes the identifier numbers of the pairs to the list view in the UI.
     */
    private synchronized void writePairListIdentNumbersToTab() {
        Platform.runLater(() -> {
            if (!pairIdentNumbersList.getItems().isEmpty()) {
                pairIdentNumbersList.getItems().clear();
            }

            ObservableList<String> data = FXCollections.observableArrayList(pairIdentNumber.asList());
            pairIdentNumbersList.setItems(data);
        });
    }

    private synchronized void writeGroupListIdentNumbersToTab() {
        Platform.runLater(() -> {
            if (!groupIdentNumbersList.getItems().isEmpty()) {
                groupIdentNumbersList.getItems().clear();
            }

            ObservableList<String> data = FXCollections.observableArrayList(groupIdentNumber.asList());
            groupIdentNumbersList.setItems(data);
        });
    }

    /**
     * Writes the successors to the table view in the UI.
     */
    private synchronized void writePairListSuccessorsToTab() {
        Platform.runLater(() -> {
            if (!successorsPairList.getItems().isEmpty()) {
                successorsPairList.getItems().clear();
            } else {
                setupParticipantSuccessorsValueFactories();
            }

            ObservableList<Participant> data = FXCollections.observableArrayList(pairList.getSuccessors());
            successorsPairList.setItems(data);
        });
    }

    private synchronized void writeGroupListSuccessorsToTab() {
        Platform.runLater(() -> {
            if (!successorsGroupList.getItems().isEmpty()) {
                successorsGroupList.getItems().clear();
            } else {
                setupPairSuccessorsValueFactories();
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(groupList.getSuccessorPairs());
            successorsGroupList.setItems(data);
        });
    }

    @FXML
    void comparePairList(ActionEvent event) {
        MainFrame.stage.hide();
        PairListComparisonController dialog = new PairListComparisonController();
        dialog.init(root.getScene().getWindow());
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setMinWidth(910); // additional space to prevent clipping
        stage.setMinHeight(635); // extra space for buttons
        stage.setResizable(true);
        stage.sizeToScene();
        try {
            this.pairList = dialog.showAndWait().orElse(pairList);
            this.pairIdentNumber = pairList.getIdentNumber();
            writePairDataToTab();
        } catch (ClassCastException | NullPointerException e) {
            MainFrame.stage.show();
            return;
        }
        MainFrame.stage.show();
    }
}
