package controller.FXMLControllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;
import model.event.collection.Group;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.identNumbers.IdentNumber;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GroupListComparisonController extends Dialog<GroupList>{

    @FXML
    private TableColumn<Group, Integer> cookPairOne;

    @FXML
    private TableColumn<Group, Integer> cookPairTwo;

    @FXML
    private TableColumn<Group, String> courseOne;

    @FXML
    private TableColumn<Group, String> courseTwo;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private MenuItem editWeightsList1MenuItem;

    @FXML
    private MenuItem editWeightsList2MenuItem;

    @FXML
    private TableColumn<Group, Integer> idOne;

    @FXML
    private TableColumn<Group, Integer> idTwo;

    @FXML
    private ListView<String> identNumbersTwo;

    @FXML
    private ListView<String> identNumbersOne;

    @FXML
    private TableColumn<Group, String> kitchenOne;

    @FXML
    private TableColumn<Group, String> kitchenTwo;

     /*
    Note that for the pair id columns the first number identifies to which table the column belongs
    i.e. pairOneX belongs to table one
     */
    @FXML
    private TableColumn<Group, Integer> pairOneOne;

    @FXML
    private TableColumn<Group, Integer> pairOneThree;

    @FXML
    private TableColumn<Group, Integer> pairOneTwo;

    @FXML
    private TableColumn<Group, Integer> pairTwoThree;

    @FXML
    private TableColumn<Group, Integer> pairTwoOne;

    @FXML
    private TableColumn<Group, Integer> pairTwoTwo;

    @FXML
    private TableView<Group> tableOne;

    @FXML
    private TableView<Group> tableTwo;

    private ButtonType acceptList1ButtonType;
    private ButtonType acceptList2ButtonType;
    private GroupList groupListOne;
    private GroupList groupListTwo;
    private PairList pairList;
    private IdentNumber gropIdentNumberOne;
    private IdentNumber gropIdentNumberTwo;

    @FXML
    private void initialize() {
        // same as in MainController
        MainController.addListenersToTable(tableOne);
        MainController.makeTableNotReorderable(tableOne);

        MainController.addListenersToTable(tableTwo);
        MainController.makeTableNotReorderable(tableTwo);
    }

    public void init(Window owner, PairList pairList) {
        this.pairList = pairList;
        try {
            String relPath = "src/main/java/view/fxml/compareGroupList.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this); // This is the most important part. Otherwise, all fields are mysteriously null

            this.acceptList1ButtonType = new ButtonType("Obere Liste übernehmen", ButtonBar.ButtonData.APPLY);
            this.acceptList2ButtonType = new ButtonType("Untere Liste übernehmen", ButtonBar.ButtonData.APPLY);

            dialogPane = loader.load();
            dialogPane.getButtonTypes().addAll(
                    acceptList1ButtonType,
                    acceptList2ButtonType,
                    ButtonType.CANCEL
            );

            setResultConverter(buttonType -> {
                if (buttonType == acceptList1ButtonType) {
                    return getGroupList1();
                }
                if (buttonType == acceptList2ButtonType) {
                    return getGroupList2();
                }
                return getResult();
            });

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(true);
            setTitle("Paar Listen vergleichen");
            setDialogPane(dialogPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GroupList getGroupList2() {
        return this.groupListTwo;
    }

    private GroupList getGroupList1() {
        return this.groupListOne;
    }


    @FXML
    void editWeightsList1(ActionEvent event) {
        GroupWeightsController dialog = new GroupWeightsController();
        dialog.init(this.dialogPane.getScene().getWindow());

        GroupWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.groupListOne = new GroupList(pairList, weights);
                this.gropIdentNumberOne = this.groupListOne.getIdentNumber();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
            writeGroupDataToTab(tableOne, groupListOne, identNumbersOne);
        }
    }

    @FXML
    void editWeightsList2(ActionEvent event) {
        GroupWeightsController dialog = new GroupWeightsController();
        dialog.init(this.dialogPane.getScene().getWindow());

        GroupWeights weights = dialog.showAndWait().orElse(null);

        if (weights != null) {
            try {
                this.groupListTwo = new GroupList(pairList, weights);
                this.gropIdentNumberTwo = this.groupListTwo.getIdentNumber();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dateifehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten!");
                alert.setContentText("Es wurden noch keine Dateien für die Teilnehmerdaten und/oder die After-Dinner-Location ausgewählt.");
                alert.showAndWait();
                return;
            }
            writeGroupDataToTab(tableTwo, groupListTwo, identNumbersTwo);
        }
    }

    private void writeGroupDataToTab(TableView<Group> tableView, GroupList groupList, ListView<String> list) {
        Platform.runLater(() -> {
            if (!tableView.getItems().isEmpty()) {
                tableView.getItems().clear();
            } else {
                setupGroupListValueFactories(
                        (TableColumn<Group, Integer>) tableView.getColumns().get(0),
                        (TableColumn<Group, Integer>) tableView.getColumns().get(1),
                        (TableColumn<Group, Integer>) tableView.getColumns().get(2),
                        (TableColumn<Group, Integer>) tableView.getColumns().get(3),
                        (TableColumn<Group, String>) tableView.getColumns().get(4),
                        (TableColumn<Group, String>) tableView.getColumns().get(5),
                        (TableColumn<Group, Integer>) tableView.getColumns().get(6)
                );
            }

            ObservableList<Group> data = FXCollections.observableArrayList(groupList.getGroups());
            tableView.setItems(data);

            writeIdentNumbersToTab(list, groupList);
        });
    }

    private void writeIdentNumbersToTab(ListView<String> list, GroupList groupList) {
        Platform.runLater(() -> {
            if (!list.getItems().isEmpty()) {
                list.getItems().clear();
            }

            ObservableList<String> data = FXCollections.observableArrayList(groupList.getIdentNumber().asList());
            list.setItems(data);
        });
    }

    private void setupGroupListValueFactories(
            TableColumn<Group, Integer> idColGroup,
            TableColumn<Group, Integer> pairOneColGroup,
            TableColumn<Group, Integer> pairTwoColGroup,
            TableColumn<Group, Integer> pairThreeColGroup,
            TableColumn<Group, String> kitchenColGroup,
            TableColumn<Group, String> courseColGroup,
            TableColumn<Group, Integer> cookIDColGroup
    ) {
        idColGroup.setCellValueFactory(cell -> cell.getValue().getIdAsObservable());
        pairOneColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[0].getIdAsObservable());
        pairTwoColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[1].getIdAsObservable());
        pairThreeColGroup.setCellValueFactory(cell -> cell.getValue().getPairs()[2].getIdAsObservable());
        kitchenColGroup.setCellValueFactory(cell -> cell.getValue().getKitchen().asObservable());
        courseColGroup.setCellValueFactory(cell -> cell.getValue().getCourse().asProperty());
        cookIDColGroup.setCellValueFactory(cell -> cell.getValue().getCookPairIdAsObservable());
    }

}
