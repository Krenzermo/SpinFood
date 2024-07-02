package controller.FXMLControllers;

import controller.LanguageController;
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
import java.util.List;

public class PairListComparisonController extends Dialog<PairList> {
    private final LanguageController languageController = LanguageController.getInstance();

    @FXML
    private TableColumn<Pair, String> courseColList1;

    @FXML
    private TableColumn<Pair, String> courseColList2;

    @FXML
    private DialogPane dialog;

    @FXML
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

    @FXML
    private TableColumn<Pair, String> per1ColList1;

    @FXML
    private TableColumn<Pair, String> per1ColList2;

    @FXML
    private TableColumn<Pair, String> per2ColList1;

    @FXML
    private TableColumn<Pair, String> per2ColList2;

    @FXML
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

    private PairList pairList1;
    private IdentNumber pairIdentNumber1;
    private PairList pairList2;
    private IdentNumber pairIdentNumber2;
    private ButtonType acceptList1ButtonType;
    private ButtonType acceptList2ButtonType;

    @FXML
    private void initialize() {
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

        if (pairList1 != null) {
            writePairDataToTab(tableList1, pairList1, identNumberList1);
        }
    }

    private PairList getPairList1() {
        return pairList1;
    }

    private PairList getPairList2() {
        return pairList2;
    }

    @FXML
    void openList1FileChooser(ActionEvent event) {
        // TODO: this
    }

    @FXML
    void openList2FileChooser(ActionEvent event) {
        // TODO: this
    }

    @FXML
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
    private void writePairDataToTab(TableView<Pair> pairTable, PairList pairList, ListView<String> listView) {
            if (!pairTable.getItems().isEmpty()) {
                pairTable.getItems().clear();
            } else {
                setupValueFactories(
		                (TableColumn<Pair, Integer>) pairTable.getColumns().get(0),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(1),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(2),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(3),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(4),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(5),
		                (TableColumn<Pair, String>) pairTable.getColumns().get(6),
		                (TableColumn<Pair, Boolean>) pairTable.getColumns().get(7)
                );
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(pairList.getPairs());
            pairTable.setItems(data);

            writeIdentNumbersToTab(listView, pairList.getIdentNumber());
    }

    /**
     * Sets up the value factories for the table columns to display the pair data.
     */
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
}

