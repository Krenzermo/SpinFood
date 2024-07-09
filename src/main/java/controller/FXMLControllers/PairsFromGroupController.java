package controller.FXMLControllers;

import controller.LanguageController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;
import model.event.Course;
import model.event.collection.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PairsFromGroupController extends Dialog<Object>{
    private final LanguageController languageController = LanguageController.getInstance();

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TableColumn<Pair, String> courseCol;

    @FXML
    private TableColumn<Pair, Integer> idCol;

    @FXML
    private TableColumn<Pair, String> kitchenCol;

    @FXML
    private TableView<Pair> pairTable;

    @FXML
    private TableColumn<Pair, String> part2Col;

    @FXML
    private TableColumn<Pair, String> partOneCol;

    private Pair[] pairs;

    @FXML
    private void initialize() {
        bindAllComponents();

        pairTable.columnResizePolicyProperty().addListener((observable, oldValue, newValue) -> MainController.adjustColumnWidths(pairTable));
        pairTable.widthProperty().addListener((observable, oldValue, newValue) -> MainController.adjustColumnWidths(pairTable));
        idCol.setReorderable(false);
        partOneCol.setReorderable(false);
        part2Col.setReorderable(false);
        kitchenCol.setReorderable(false);
        courseCol.setReorderable(false);
    }

    private void bindAllComponents() {
        // TODO: this
    }

    public void init(Window owner, Pair[] pairs) {
        try {
            String relPath = "src/main/java/view/fxml/pairsFromGroup.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);

            dialogPane = loader.load();
            this.pairs = pairs;

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setTitle("Paare");
            setDialogPane(dialogPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized void writePairDataToTab() {
        Platform.runLater(() -> {
            if (!pairTable.getItems().isEmpty()) {
                pairTable.getItems().clear();
            } else {
                setupPairListValueFactories();
            }

            ObservableList<Pair> data = FXCollections.observableArrayList(pairs);
            pairTable.setItems(data);
        });
    }

    protected void setupPairListValueFactories() {
        idCol.setCellValueFactory(
                cell -> cell.getValue().getIdAsObservable()
        );

        partOneCol.setCellValueFactory(
                cell -> cell.getValue().getParticipants().get(0).getName().asObservable()
        );
        part2Col.setCellValueFactory(
                cell -> cell.getValue().getParticipants().get(1).getName().asObservable()
        );

        kitchenCol.setCellValueFactory(
                cell -> cell.getValue().getKitchen().asObservable()
        );

        courseCol.setCellValueFactory(
                cell -> {
                    Course course = cell.getValue().getCourse();
                    if (course == null) {
                        return new SimpleStringProperty("n.V.");
                    }
                    return course.asObservable();
                }
        );
    }
}


