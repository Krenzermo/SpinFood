package controller.FXMLControllers;

<<<<<<< HEAD
=======
import controller.LanguageController;
>>>>>>> Davide
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;
import model.event.Course;
import model.event.collection.Pair;
import model.event.list.PairList;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.PairingWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;
<<<<<<< HEAD

public class PairListComparisonController extends Dialog<PairList> {
=======
import java.util.List;

public class PairListComparisonController extends Dialog<PairList> {
    private final LanguageController languageController = LanguageController.getInstance();

>>>>>>> Davide
    @FXML
    private TableColumn<Pair, String> courseColList1;

    @FXML
    private TableColumn<Pair, String> courseColList2;

    @FXML
    private DialogPane dialog;

    @FXML
<<<<<<< HEAD
    private MenuItem editWeightsList1;

    @FXML
    private MenuItem editWeightsList2;

    @FXML
    private TableColumn<Pair, String> idColList1;

    @FXML
    private TableColumn<Pair, String> idColList2;

    @FXML
    private ListView<String> identNumberList1;

    @FXML
    private ListView<String> identNumbersList2;

    @FXML
    private TableColumn<Pair, String> kitchenColList1;

    @FXML
    private TableColumn<Pair, String> kitchenColList2;
=======
    private MenuItem openList1MenuItem;

    @FXML
    private MenuItem openList2MenuItem;

    @FXML
    private MenuItem editWeightsList1MenuItem;

    @FXML
    private MenuItem editWeightsList2MenuItem;

    @FXML
    private TableView<Pair> tableList1;

    @FXML
    private TableView<Pair> tableList2;

    @FXML
    private TableColumn<Pair, Integer> idColList1;

    @FXML
    private TableColumn<Pair, Integer> idColList2;
>>>>>>> Davide

    @FXML
    private TableColumn<Pair, String> per1ColList1;

    @FXML
    private TableColumn<Pair, String> per1ColList2;

    @FXML
    private TableColumn<Pair, String> per2ColList1;

    @FXML
    private TableColumn<Pair, String> per2ColList2;

    @FXML
<<<<<<< HEAD
    private TableView<Pair> tableList1;

    @FXML
    private TableView<Pair> tableList2;
=======
    private TableColumn<Pair, String> gender1ColList1;

    @FXML
    private TableColumn<Pair, String> gender1ColList2;

    @FXML
    private TableColumn<Pair, String> gender2ColList1;

    @FXML
    private TableColumn<Pair, String> gender2ColList2;

    @FXML
    private TableColumn<Pair, String> kitchenColList1;

    @FXML
    private TableColumn<Pair, String> kitchenColList2;

    @FXML
    private TableColumn<Pair, String> foodTypeColList1;

    @FXML
    private TableColumn<Pair, String> foodTypeColList2;

    @FXML
    private TableColumn<Pair, Boolean> signedUpTogetherColList1;

    @FXML
    private TableColumn<Pair, Boolean> signedUpTogetherColList2;

    @FXML
    private TitledPane identNumbersTableList1;

    @FXML
    private TitledPane identNumbersTableList2;

    @FXML
    private ListView<String> identNumberList1;

    @FXML
    private ListView<String> identNumbersList2;
>>>>>>> Davide

    private PairList pairList1;
    private IdentNumber pairIdentNumber1;
    private PairList pairList2;
    private IdentNumber pairIdentNumber2;
    private ButtonType acceptList1ButtonType;
    private ButtonType acceptList2ButtonType;

    @FXML
    private void initialize() {
<<<<<<< HEAD
        // same as in MainController
        tableList1.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(tableList1));
        tableList1.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(tableList1));
        idColList1.setReorderable(false);
        per1ColList1.setReorderable(false);
        per2ColList1.setReorderable(false);
        kitchenColList1.setReorderable(false);
        courseColList1.setReorderable(false);

        tableList2.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(tableList2));
        tableList2.widthProperty().addListener((observable, oldValue, newValue) -> adjustColumnWidths(tableList2));
        idColList2.setReorderable(false);
        per1ColList2.setReorderable(false);
        per2ColList2.setReorderable(false);
        kitchenColList2.setReorderable(false);
        courseColList2.setReorderable(false);
    }

    // copy contained in MainController
    private static <E> void adjustColumnWidths(TableView<E> tableView) {
        long visibleColumns = tableView.getColumns().stream().filter(TableColumn::isVisible).count();
        if (visibleColumns > 0) {
            double newWidth = tableView.getWidth() / visibleColumns;
            for (TableColumn<E, ?> column : tableView.getColumns()) {
                if (column.isVisible()) {
                    column.setPrefWidth(newWidth);
                }
            }
        }
=======
        bindAllComponents();

        MainController.addListenersToTable(tableList1);
        MainController.makeTableNotReorderable(tableList1);
        gender1ColList1.setVisible(false);
        gender2ColList1.setVisible(false);
        kitchenColList1.setVisible(false);

        MainController.addListenersToTable(tableList2);
        MainController.makeTableNotReorderable(tableList2);
        gender1ColList2.setVisible(false);
        gender2ColList2.setVisible(false);
        kitchenColList2.setVisible(false);
    }

    private void bindAllComponents() {
        // TODO: this
>>>>>>> Davide
    }

    public void init(Window owner) {
        try {
            String relPath = "src/main/java/view/fxml/comparePairList.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this); // This is the most important part. Otherwise, all fields are mysteriously null

            this.acceptList1ButtonType = new ButtonType("Obere Liste übernehmen", ButtonBar.ButtonData.APPLY);
            this.acceptList2ButtonType = new ButtonType("Untere Liste übernehmen", ButtonBar.ButtonData.APPLY);

            dialog = loader.load();
            dialog.getButtonTypes().addAll(
                    acceptList1ButtonType,
                    acceptList2ButtonType,
                    ButtonType.CANCEL
            );

            setResultConverter(buttonType -> {
                if (buttonType == acceptList1ButtonType) {
                    return getPairList1();
                }
                if (buttonType == acceptList2ButtonType) {
                    return getPairList2();
                }
                return getResult();
            });

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(true);
            setTitle("Paar Listen vergleichen");
            setDialogPane(dialog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
<<<<<<< HEAD
=======

        if (pairList1 != null) {
            writePairDataToTab(tableList1, pairList1, identNumberList1);
        }
>>>>>>> Davide
    }

    private PairList getPairList1() {
        return pairList1;
    }

    private PairList getPairList2() {
        return pairList2;
    }

    @FXML
<<<<<<< HEAD
=======
    void openList1FileChooser(ActionEvent event) {
        // TODO: not necessary
    }

    @FXML
    void openList2FileChooser(ActionEvent event) {
        // TODO: not necessary
    }

    @FXML
>>>>>>> Davide
    void editWeightsList1(ActionEvent event) {
        String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        PairingWeightsController dialog = new PairingWeightsController();
        dialog.init(this.dialog.getScene().getWindow());

        PairingWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.pairList1 = new PairList(weights);
                this.pairIdentNumber1 = this.pairList1.getIdentNumber();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
            writePairDataToTab(tableList1, pairList1, identNumberList1);
        }
        //event.consume();
    }

    @FXML
    void editWeightsList2(ActionEvent event) {
        String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
        File file = new File(relPath);
        String absPath = file.getAbsolutePath();
        PairingWeightsController dialog = new PairingWeightsController();
        dialog.init(this.dialog.getScene().getWindow());

        PairingWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.pairList2 = new PairList(weights);
                pairIdentNumber2 = pairList2.getIdentNumber();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }

            writePairDataToTab(tableList2, pairList2, identNumbersList2);
        }
        //event.consume();
    }


    /**
     * Writes the pair data to the table in the UI.
     * Clears existing items if necessary and sets up value factories for the table columns.
     */
<<<<<<< HEAD
    protected void writePairDataToTab(TableView<Pair> pairTable, PairList pairList, ListView<String> listView) {
=======
    private void writePairDataToTab(TableView<Pair> pairTable, PairList pairList, ListView<String> listView) {
>>>>>>> Davide
            if (!pairTable.getItems().isEmpty()) {
                pairTable.getItems().clear();
            } else {
                setupValueFactories(
<<<<<<< HEAD
                        (TableColumn<Pair, String>) pairTable.getColumns().get(0),
                        (TableColumn<Pair, String>) pairTable.getColumns().get(1),
                        (TableColumn<Pair, String>) pairTable.getColumns().get(2),
                        (TableColumn<Pair, String>) pairTable.getColumns().get(3),
                        (TableColumn<Pair, String>) pairTable.getColumns().get(4)
=======
		                (TableColumn<Pair, Integer>) pairTable.getColumns().get(0),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(1),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(2),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(3),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(4),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(5),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(6),
		                (TableColumn<Pair, Boolean>) pairTable.getColumns().get(7)
>>>>>>> Davide
                );
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(pairList.getPairs());
            pairTable.setItems(data);

            writeIdentNumbersToTab(listView, pairList.getIdentNumber());
    }

    /**
     * Sets up the value factories for the table columns to display the pair data.
     */
<<<<<<< HEAD
    protected void setupValueFactories(
            TableColumn<Pair, String> idColPair,
            TableColumn<Pair, String> partOneColPair,
            TableColumn<Pair, String> partTwoColPair,
            TableColumn<Pair, String> kitchenColPair,
            TableColumn<Pair, String> courseColPair
    ) {
        idColPair.setCellValueFactory(
                cell -> cell.getValue().getIdAsProperty()
        );

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
                cell -> {
                    Course course = cell.getValue().getCourse();
                    if (course == null) {
                        return new SimpleStringProperty("n.V.");
                    }
                    return course.asProperty();
                }
        );
=======
    private void setupValueFactories(
            TableColumn<Pair, Integer> idColPair,
            TableColumn<Pair, String> partOneColPair,
            TableColumn<Pair, String> partTwoColPair,
            TableColumn<Pair, String> genderOneColPair,
            TableColumn<Pair, String> genderTwoColPair,
            TableColumn<Pair, String> kitchenColPair,
            TableColumn<Pair, String> foodTypeColPair,
            TableColumn<Pair, Boolean> signedUpTogether
    ) {
        idColPair.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        partOneColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getName().asObservable());
        partTwoColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getName().asObservable());
        genderOneColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(0).getGender().asObservable());
        genderTwoColPair.setCellValueFactory(cell -> cell.getValue().getParticipants().get(1).getGender().asObservable());
        kitchenColPair.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        foodTypeColPair.setCellValueFactory(cell -> cell.getValue().getFoodType().asObservable());
        signedUpTogether.setCellValueFactory(cell -> cell.getValue().getSignedUpTogetherAsObservable());
>>>>>>> Davide
    }

    /**
     * Writes the identifier numbers of the pairs to the list view in the UI.
     */
    private void writeIdentNumbersToTab(ListView<String> pairIdentNumbersList, IdentNumber pairIdentNumber) {
            if (!pairIdentNumbersList.getItems().isEmpty()) {
                pairIdentNumbersList.getItems().clear();
            }

            ObservableList<String> data = FXCollections.observableArrayList(pairIdentNumber.asList());
            pairIdentNumbersList.setItems(data);
    }
<<<<<<< HEAD
=======

    /**
     * Call this method before calling init()
     *
     * @param list The {@link PairList} to be set as pairList1
     */
    public void setInitialList1(PairList list) {
        if (list == null) {
            return;
        }
        pairList1 = list;
        pairIdentNumber1 = pairList1.getIdentNumber();
    }

    /**
     * Call this method before calling init()
     *
     * @param list The {@link List} of {@link Pair} instances to be set as pairList1
     */
    public void setInitialList1(List<Pair> list) {
        if (list == null) {
            return;
        }
        pairList1 = new PairList(list);
        pairIdentNumber1 = pairList1.getIdentNumber();
    }
>>>>>>> Davide
}

