package controller.FXMLControllers;

import controller.LanguageController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


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
import model.processing.states.State;
import view.MainFrame;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * MainController class handles the primary logic for managing pairs and groups in the MainFrame of the application.
 * It manages the loading of participant lists, party locations, and executing algorithms to create pairs and groups.
 */
public class MainController {
    private final InputData inputData = InputData.getInstance();
    private final LanguageController languageController = LanguageController.getInstance();
    private PairList pairList;
    private GroupList groupList; // Added groupList for group-related operations
    private IdentNumber pairIdentNumber;
	private IdentNumber groupIdentNumber;
    private PairingWeights pairingWeights;
    private GroupWeights groupWeights; // Added groupWeights for group-related operations

    private volatile String participantListPath = null;
    private volatile String locationPath = null;

    private State state = new State(pairList, groupList);

    @FXML
    private VBox root;

    @FXML
    private MenuItem openParticipantList;

    @FXML
    private MenuItem openPartyLoc;

    @FXML
    private MenuItem openCancellationsList;

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
    private TitledPane identNumbersPairList;

    @FXML
    private TitledPane pairListSuccessors;

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
    private TableColumn<Group, Integer> cookIDColGroup;

    @FXML
    private TitledPane identNumbersGroupList;

    @FXML
    private TitledPane groupListSuccessors;

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
    private MenuItem comparePairListMenuItem;

    @FXML
    private MenuItem compareGroupListMenuItem;

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

    @FXML
    private MenuItem changeLanguageGerman;

    @FXML
    private MenuItem changeLanguageEnglish;

    @FXML
    private MenuItem savePairListMenuItem;

    @FXML
    private MenuItem saveGroupListMenuItem;

    @FXML
    private MenuItem savePartSuccessorsMenuItem;

    @FXML
    private MenuItem savePairSuccessorsMenuItem;

    @FXML
    private Button undo;

    //private Stage primaryStage = (Stage) root.getScene().getWindow();

    @FXML
    public void initialize() {
        bindAllComponents();

        undo.setDisable(true);

        addListenersToTable(pairTable);
        makeTableNotReorderable(pairTable);
	    genderOneColPair.setVisible(false);
	    genderTwoColPair.setVisible(false);
	    kitchenColPair.setVisible(false);

	    addListenersToTable(groupTable);
	    makeTableNotReorderable(groupTable);
	    kitchenColGroup.setVisible(false);

	    addListenersToTable(successorsPairList);
	    makeTableNotReorderable(successorsPairList);
	    kitchenParticipantSuccessors.setVisible(false);

	    addListenersToTable(successorsGroupList);
	    makeTableNotReorderable(successorsGroupList);
	    genderOneColPairSuccessors.setVisible(false);
	    genderTwoColPairSuccessors.setVisible(false);
	    kitchenColPairSuccessors.setVisible(false);

	    pairTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    groupTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    successorsPairList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    successorsGroupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	    pairTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Pair>) change -> changeSplitPairButtonActivity());
	    pairTable.focusedProperty().addListener((observableValue, oldValue, newValue) -> changeSplitPairButtonActivity());

	    groupTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Group>) change -> changeSplitGroupButtonActivity());
	    groupTable.focusedProperty().addListener((observableValue, oldValue, newValue) -> changeSplitGroupButtonActivity());

	    successorsPairList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Participant>) change -> changeCreatePairButtonActivity());
	    successorsPairList.focusedProperty().addListener((observableValue, oldValue, newValue) -> changeCreatePairButtonActivity());

	    successorsGroupList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Pair>) change -> changeCreateGroupButtonActivity());
	    successorsGroupList.focusedProperty().addListener((observableValue, oldValue, newValue) -> changeCreateGroupButtonActivity());


        /*
        // Sadly, this does not work :(

        idColGroup.setCellValueFactory(cell -> new TableCell<Pair, Integer>() {
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    cell.getValue().getCookPairIdAsObservable();
                    setOnMouseClicked(event -> {
                        try {
                            PairsFromGroupController controller = new PairsFromGroupController();
                            controller.init(root.getScene().getWindow(), groupList.getPairList().toArray(new Pair[0]));
                            controller.writePairDataToTab();
                            controller.showAndWait();
                        } catch (NullPointerException e) {
                            return;
                        }
                    });
                }
            }
        }.itemProperty());


        idColGroup.addEventHandler(ActionEvent.ANY, (event) -> {
            try {
                PairsFromGroupController controller = new PairsFromGroupController();
                controller.init(root.getScene().getWindow(), groupList.getPairList().toArray(new Pair[0]));
                controller.writePairDataToTab();
                controller.showAndWait();
            } catch (NullPointerException e) {
                return;
            }
        });

 */

	    groupTable.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
		    try {
			    PairsFromGroupController controller = new PairsFromGroupController();
			    controller.init(root.getScene().getWindow(), newVal.getPairs());
			    controller.writePairDataToTab();
			    controller.showAndWait();
		    } catch (NullPointerException e) {
			    return;
		    }
	    });

	    partOneColPair.setCellFactory(column -> new TableCell<>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
			    super.updateItem(item, empty);
			    if (item == null || empty) {
				    setText(null);
			    } else {
				    setText(item);
				    setOnMouseClicked(event -> showUnsubscriberDialog(getTableRow().getItem().getParticipants().get(0)));
			    }
		    }
	    });

	    partTwoColPair.setCellFactory(column -> new TableCell<>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
			    super.updateItem(item, empty);
			    if (item == null || empty) {
				    setText(null);
			    } else {
				    setText(item);
				    setOnMouseClicked(event -> showUnsubscriberDialog(getTableRow().getItem().getParticipants().get(1)));
			    }
		    }
	    });
    }

    private void bindAllComponents() {
        languageController.bindComponent(pairTab, "tab.pairTab");
        languageController.bindComponent(pairTable, "table.default");

        // TODO: this
    }


    protected static <E> void addListenersToTable(TableView<E> tableView) {
        tableView.widthProperty().addListener((observableValue, oldValue, newValue) -> MainController.adjustColumnWidths(tableView));
        for (TableColumn<E, ?> column : tableView.getColumns()) {
            column.visibleProperty().addListener((observableValue, oldValue, newValue) -> MainController.adjustColumnWidths(tableView));
        }
    }

    protected static <E> void makeTableNotReorderable(TableView<E> tableView) {
        for (TableColumn<E, ?> column : tableView.getColumns()) {
             column.setReorderable(false);
        }
    }

    // copy contained in PairListComparisonController
    protected static <E> void adjustColumnWidths(TableView<E> tableView) {
        long visibleColumns = tableView.getColumns().stream().filter(TableColumn::isVisible).count();
        if (visibleColumns > 0) {
            double newWidth = (tableView.getWidth() -18) / visibleColumns; // 18px extra space for tableMenuButton
            for (TableColumn<E, ?> column : tableView.getColumns()) {
                if (column.isVisible()) {
                    column.setPrefWidth(newWidth);
                    //column.setMinWidth(newWidth);
                    //column.setMaxWidth(newWidth);
                }
            }
        }
    }

    private void changeSplitPairButtonActivity() {
        if (pairTable.getSelectionModel().getSelectedItems().size() == 1 && (pairTable.isFocused() || splitPairButton.isFocused())) {
            splitPairButton.setDisable(false);
        } else {
            splitPairButton.setDisable(true);
        }
    }

    private void changeSplitGroupButtonActivity() {
        if (groupTable.getSelectionModel().getSelectedItems().size() == 1 && (groupTable.isFocused() || splitGroupButton.isFocused())) {
            splitGroupButton.setDisable(false);
        } else {
            splitGroupButton.setDisable(true);
        }
    }

    private void changeCreatePairButtonActivity() {
        if (successorsPairList.getSelectionModel().getSelectedItems().size() == 2 && (successorsPairList.isFocused() || createPairButton.isFocused())) {
            createPairButton.setDisable(false);
        } else {
            createPairButton.setDisable(true);
        }
    }

    private void changeCreateGroupButtonActivity() {
        if (successorsGroupList.getSelectionModel().getSelectedItems().size() == 9 && (successorsGroupList.isFocused() || createGroupButton.isFocused())) {
            createGroupButton.setDisable(false);
        } else {
            createGroupButton.setDisable(true);
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
        undo.setDisable(true);
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
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dateifehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten!");
            alert.setContentText("Es wurde eine falsche oder fehlerhafte Datei für die Teilnehmerliste ausgewählt.");
            alert.showAndWait();
        }
    }

    @FXML
    void openFileChooserCancellationsList(ActionEvent event) {
        // TODO: this
    }

    @FXML
    void changeLanguageToGerman(ActionEvent event) {
        if (!languageController.getLanguage().equals(Locale.GERMAN)) {
            languageController.setLanguage(Locale.GERMAN);
        }
    }


    @FXML
    void changeLanguageToEnglish(ActionEvent event) {
        if (!languageController.getLanguage().equals(Locale.ENGLISH)) {
            languageController.setLanguage(Locale.ENGLISH);
        }
    }



    @FXML
    void savePairList(ActionEvent event) {
        // TODO: this
    }

    @FXML
    void saveGroupList(ActionEvent event) {
        // TODO: this
    }

    @FXML
    void saveParticipantSuccessors(ActionEvent event) {
        // TODO: this
    }

    @FXML
    void savePairSuccessorsMenuItem(ActionEvent event) {
        // TODO: this
    }

    /**
     * Opens a file chooser for selecting the party location file.
     * Starts a monitoring thread if it's not already running.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    void openFileChooserPartLoc(ActionEvent event) {
        undo.setDisable(true);
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
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dateifehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten!");
            alert.setContentText("Es wurde eine falsche oder fehlerhafte Datei für die After-Dinner-Location ausgewählt.");
            alert.showAndWait();
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
        undo.setDisable(true);
        String relPath = "src/main/java/view/fxml/groupWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        GroupWeightsController dialog = new GroupWeightsController();
        dialog.init(root.getScene().getWindow());

        GroupWeights weights = dialog.showAndWait().orElse(null);
        this.groupWeights = weights;

        if (weights != null) {
            try {
                this.groupList = new GroupList(pairList, weights);
                this.groupIdentNumber = this.groupList.getIdentNumber();
                this.pairList = groupList.getPairList();
                this.pairIdentNumber = pairList.getIdentNumber();

                updateGroupTable();

                updatePairTable();

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
        state.updateState(pairList);
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
        undo.setDisable(true);
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
                updatePairTable();

                replaceGroupData();

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
        if (groupList != null) {
            state.updateState(pairList);
        }
        event.consume();
    }

    @FXML
    void splitPair(ActionEvent event) {
        Pair pair = pairTable.getSelectionModel().getSelectedItem();
        removePair(pair);
        pairList.getSuccessors().add(pair.getParticipants().get(0));
        pairList.getSuccessors().add(pair.getParticipants().get(1));
        updatePairTable();
        if (!Objects.isNull(groupList)) {
            updateGroupTable();
        }
        if (groupList != null) {
            undo.setDisable(false);
            state.updateState(pairList);
        }

    }

    @FXML
    void createPair(ActionEvent event) {
        List<Participant> participants = successorsPairList.getSelectionModel().getSelectedItems();
        // assert participants.size() == 2;
        if (participants.size() != 2) { // this should never happen
            throw new IllegalStateException("More or less than 2 successor participants were selected. Total: " + participants.size());
        }

        pairList.add(new Pair(participants.get(0), participants.get(1), pairList.getPairIdCounterAndIncrement()));
        pairList.getSuccessors().removeAll(participants);
        updatePairTable();

        if (groupList != null) {
            undo.setDisable(false);
            state.updateState(pairList);
        }
    }

    @FXML
    public void splitGroup(ActionEvent event) {
        Group group = groupTable.getSelectionModel().getSelectedItem();
        HashSet<Group> groupCluster = new HashSet<>();
        Arrays.stream(group.getPairs())
                .sequential()
                .flatMap(pair -> pair.getGroups().stream())
                .distinct()
                .flatMap(group1 -> Arrays.stream(group1.getPairs()).sequential())
                .distinct()
                .flatMap(pair -> pair.getGroups().stream())
                .distinct()
                .forEach(groupCluster::add);

        HashSet<Pair> pairs = new HashSet<>(groupCluster.stream().flatMap(group1 -> Arrays.stream(group1.getPairs()).sequential().distinct()).toList());

        removeGroupCluster(groupCluster.stream().toList());

        groupList.getSuccessorPairs().addAll(pairs);

        updateGroupTable();

        undo.setDisable(false);
        state.updateState(pairList);
    }

    @FXML
    public void createGroup(ActionEvent event) {
        List<Pair> pairs = successorsGroupList.getSelectionModel().getSelectedItems();
        // assert pairs.size() == 9;
        if (pairs.size() != 9) {
            throw new IllegalStateException("More or less than 9 successor pairs were selected. Total: " + pairs.size());
        }

        //pairs.forEach(Pair::clearGroups);
        // TODO: fix this
        List<Group> list = GroupList.getGroups(groupList, pairs, groupWeights); // this is a hack and not a permanent solution

        groupList.addAll(list);

        groupList.getSuccessorPairs().removeAll(pairs);

        updateGroupTable();

        undo.setDisable(false);
        state.updateState(pairList);
    }

    private void removePair(Pair pair) {
        pairList.remove(pair);

        if (!Objects.isNull(groupList)) {
            for (Group group : pair.getGroups()) {
                groupList.remove(group);

                for (Pair PairTemp : group.getPairs()) {
                    PairTemp.clearGroups();
                }
            }
        }
    }

    private void removeGroupCluster(List<Group> groups) {
        if (groups.size() != 9) {
            throw new IllegalStateException("More or less than 9 successor groups were selected. Total: " + groups.size());
        }
        for (Group group : groups) {
            groupList.remove(group);

            for (Pair pair : group.getPairs()) {
                pair.clearGroups();
            }
        }
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

	        replaceGroupData();
        });
    }

    private void replaceGroupData() {
        if (!groupTable.getItems().isEmpty()) {
            this.groupList = new GroupList(pairList, groupWeights);
            this.groupIdentNumber = groupList.getIdentNumber();

            updateGroupTable();
        }
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
        cookIDColGroup.setCellValueFactory(cell -> cell.getValue().getCookPairIdAsObservable());
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
        undo.setDisable(true);
        MainFrame.stage.hide();
        PairListComparisonController dialog = new PairListComparisonController();
        if (!Objects.isNull(pairList) && !pairList.isEmpty()) {
            dialog.setInitialList1(pairList);
        }
        dialog.init(root.getScene().getWindow());
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setMinWidth(910); // additional space to prevent clipping
        stage.setMinHeight(635); // extra space for buttons
        stage.setResizable(true);
        stage.sizeToScene();
        try {
            PairList temp = dialog.showAndWait().orElse(pairList);
            if (temp != pairList) {
                pairList = temp;
                this.pairIdentNumber = pairList.getIdentNumber();
                updatePairTable();

                tabPane.getSelectionModel().select(pairTab);
	            replaceGroupData();
            }
        } catch (ClassCastException | NullPointerException e) {
            MainFrame.stage.show();
            return;
        }
        MainFrame.stage.show();

        state.updateState(pairList);
    }

    @FXML
    void compareGroupList(ActionEvent event) {
        undo.setDisable(true);
        MainFrame.stage.hide();
        GroupListComparisonController dialog = new GroupListComparisonController();

        dialog.init(root.getScene().getWindow(), pairList);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setMinWidth(910); // additional space to prevent clipping
        stage.setMinHeight(635); // extra space for buttons
        stage.setResizable(true);
        stage.sizeToScene();
        try {
            GroupList temp = dialog.showAndWait().orElse(groupList);
            if (temp != groupList) {
                groupList = temp;
                this.groupIdentNumber = groupList.getIdentNumber();
                this.groupWeights = (GroupWeights) groupList.getWeights();
                updateGroupTable();

                pairList = groupList.getPairList();
                pairIdentNumber = pairList.getIdentNumber();

                updatePairTable();

                tabPane.getSelectionModel().select(groupTab);
            }
        } catch (ClassCastException | NullPointerException e) {
            MainFrame.stage.show();
            return;
        }
        MainFrame.stage.show();

        state.updateState(pairList);
    }

    private void showUnsubscriberDialog(Participant participant) {
        undo.setDisable(true);
        try {
            String relPath = "src/main/java/view/fxml/Unsubscriber.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);

            DialogPane dialogPane = loader.load();
            UnsubscriberController controller = loader.getController();
            controller.initData(participant, pairList, groupList, pairingWeights, groupWeights, root.getScene().getWindow());

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(dialogPane));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

            updatePairTable(); // Update tables after the dialog is closed to reflect changes

            state.updateState(pairList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePairTable() {
        writePairDataToTab();
        writePairListIdentNumbersToTab();
        writePairListSuccessorsToTab();
    }

    private void updateGroupTable() {
        writeGroupDataToTab();
        writeGroupListIdentNumbersToTab();
        writeGroupListSuccessorsToTab();
    }

    @FXML
    void goBackState(ActionEvent event) {
        this.state = state.revertState();

        groupList = new GroupList(state.getPairList());
        pairList = groupList.getPairList();

        groupIdentNumber = groupList.getIdentNumber();
        pairIdentNumber = pairList.getIdentNumber();

        updatePairTable();
        updateGroupTable();

        if (state.getPrev() == null) {
            undo.setDisable(true);
        }
    }
}
