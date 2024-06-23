package controller.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.event.list.weight.PairingWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class PairingWeightsController extends Dialog<PairingWeights> {

    public static Stage stage;
    public AnchorPane pairingWeightsPane;

    @FXML
    private DialogPane root;

    @FXML
    Button acceptPairingWeightsButton;

    @FXML
    private Slider ageDiffWeightSliderPair;

    @FXML
    private Slider foodPrefWeightSliderPair;

    @FXML
    private Slider genderDivWeightSliderPair;

    @FXML
    private void initialize() {
        ageDiffWeightSliderPair.setValue(1.0);
        genderDivWeightSliderPair.setValue(1.0);
        foodPrefWeightSliderPair.setValue(1.0);
    }

    public void init(Window owner) {
        try {
            String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);

            loader.setController(this);
            DialogPane pane = loader.load();

            pane.getButtonTypes().addAll(ButtonType.CLOSE);
            pane.getButtonTypes().addAll(ButtonType.APPLY);

            pane.lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ANY, this::pairingWeightsAccepted);
            setResultConverter(buttonType -> {
                if(!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                    return null;
                }

                return getPairingWeights();
            });

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setTitle("Paar Parameter einstellen");
            setDialogPane(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void pairingWeightsAccepted(ActionEvent event) {
        setResult(getPairingWeights());
        event.consume();
        close();
    }

    private PairingWeights getPairingWeights() {
        return new PairingWeights(
                ageDiffWeightSliderPair.getValue(),
                genderDivWeightSliderPair.getValue(),
                foodPrefWeightSliderPair.getValue()
        );
    }
}
