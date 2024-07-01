package controller.FXMLControllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MainController class handles the primary logic for managing pairs and groups in the MainFrame of the application.
 * It manages the loading of participant lists, party locations, and executing algorithms to create pairs and groups.
 */
public class MainController {
    private InputData inputData = InputData.getInstance();
    private PairList pairList;
    private GroupList groupList;
    private PairingWeights pairingWeights;
    private GroupWeights groupWeights;
    private IdentNumber pairIdentNumber;

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
    private TableView<Pair> pairTable;

    @FXML
    private TableColumn<Pair, String> partOneColPair;

    @FXML
    private TableColumn<Pair, String> partTwoColPair;

    @FXML
    private TableColumn<Pair, String> kitchenColPair;

    @FXML
    private TableColumn<Pair, String> courseColPair;

    @FXML
    private ListView<String> successorListPair;

    @FXML
    private MenuItem createGroups;

    @FXML
    private SplitPane pairSplitPane;

    @FXML
    private TableView<Group> groupTable;

    @FXML
    private TableColumn<Group, String> pairOneColGroup;

    @FXML
    private TableColumn<Group, String> pairTwoColGroup;

    @FXML
    private TableColumn<Group, String> pairThreeColGroup;

    @FXML
    private TableColumn<Group, String> kitchenColGroup;

    @FXML
    private TableColumn<Group, String> courseColGroup;

    @FXML
    public void initialize() {
        pairTable.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        partOneColPair.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        partTwoColPair.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        kitchenColPair.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));
        courseColPair.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(pairTable));

        partOneColPair.setReorderable(false);
        partTwoColPair.setReorderable(false);
        kitchenColPair.setReorderable(false);
        courseColPair.setReorderable(false);

        groupTable.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        pairOneColGroup.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        pairTwoColGroup.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        pairThreeColGroup.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        kitchenColGroup.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));
        courseColGroup.visibleProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(groupTable));

        pairOneColGroup.setReorderable(false);
        pairTwoColGroup.setReorderable(false);
        pairThreeColGroup.setReorderable(false);
        kitchenColGroup.setReorderable(false);
        courseColGroup.setReorderable(false);

        setupPairTableClickHandlers();
    }

    private <E> void adjustColumnWidths(TableView<E> tableView) {
        long visibleColumns = tableView.getColumns().stream().filter(TableColumn::isVisible).count();
        if (visibleColumns > 0) {
            double newWidth = tableView.getWidth() / visibleColumns;
            for (TableColumn<E, ?> column : tableView.getColumns()) {
                if (column.isVisible()) {
                    column.setPrefWidth(newWidth);
                }
            }
        }
    }

    @FXML
    void openFileChooserPartList(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV-Dateien (*.csv)", "*.csv");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Textdateien (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().addAll(csvFilter, txtFilter);
        fileChooser.setTitle("Öffnen Sie die Teilnehmerliste");
        try {
            participantListPath = fileChooser.showOpenDialog(root.getScene().getWindow()).getAbsolutePath();
            inputData.initParticipants(participantListPath);
        } catch (NullPointerException e) {
            participantListPath = null;
        }
    }

    @FXML
    void openFileChooserPartLoc(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV-Dateien (*.csv)", "*.csv");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Textdateien (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().addAll(csvFilter, txtFilter);
        fileChooser.setTitle("Öffnen Sie die Partylocation");
        try {
            locationPath = fileChooser.showOpenDialog(root.getScene().getWindow()).getAbsolutePath();
            inputData.initEventLocation(locationPath);
        } catch (NullPointerException e) {
            locationPath = null;
        }
    }

    @FXML
    void executeGroupAlgo(ActionEvent event) {
        //TODO: implement
    }

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
                this.pairingWeights = weights;
                this.pairList = new PairList(inputData, weights);
                this.pairIdentNumber = this.pairList.getIdentNumber();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
            writePairDataToTab();
        }
        event.consume();
    }

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

    private void setupValueFactories() {
        partOneColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getName().asProperty());
        partTwoColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getName().asProperty());
        kitchenColPair.setCellValueFactory(cell -> cell.getValue().getKitchen().asProperty());
        courseColPair.setCellValueFactory(cell -> {
            Course course = cell.getValue().getCourse();
            if (course == null) {
                return new SimpleStringProperty("n.V.");
            }
            return course.asProperty();
        });
    }

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
                        pairList.getSuccessors().stream().map(Participant::toString).collect(Collectors.toList())
                );
            }
            successorListPair.setItems(data);
        });
    }

    private void setupPairTableClickHandlers() {
        partOneColPair.setCellFactory(column -> {
            TableCell<Pair, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setGraphic(null);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Pair pair = pairTable.getItems().get(cell.getIndex());
                    Participant participant = pair.getParticipants().get(0);
                    openUnsubscriberDialog(participant);
                }
            });
            return cell;
        });

        partTwoColPair.setCellFactory(column -> {
            TableCell<Pair, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setGraphic(null);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Pair pair = pairTable.getItems().get(cell.getIndex());
                    Participant participant = pair.getParticipants().get(1);
                    openUnsubscriberDialog(participant);
                }
            });
            return cell;
        });
    }

    private void openUnsubscriberDialog(Participant participant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/unsubscriber.fxml"));
            DialogPane dialogPane = loader.load();

            UnsubscriberController controller = loader.getController();
            controller.initData(participant, pairList, groupList, pairingWeights, groupWeights);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(root.getScene().getWindow());
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
