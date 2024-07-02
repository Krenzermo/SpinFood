package controller.FXMLControllers;

import controller.LanguageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.event.list.weight.PairingWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class PairingWeightsController extends Dialog<PairingWeights> {
    private final LanguageController languageController = LanguageController.getInstance();

    @FXML
    private DialogPane root;

    @FXML
    private Text headerText;

    @FXML
    private Text textAgeDifferenceSlider;

    @FXML
    private Text textGenderDifferenceSlider;

    @FXML
    private Text textFoodPreferenceSlider;

    @FXML
    private Slider ageDiffWeightSliderPair;

    @FXML
    private Slider genderDiffWeightSliderPair;

    @FXML
    private Slider foodPrefWeightSliderPair;

    @FXML
    private void initialize() {
        bindAllComponents();

        ageDiffWeightSliderPair.setValue(1.0);
        genderDiffWeightSliderPair.setValue(1.0);
        foodPrefWeightSliderPair.setValue(1.0);
    }

    private void bindAllComponents() {
        languageController.bindComponent(headerText, "dialog.pairingWeights.headerText");
        languageController.bindComponent(textAgeDifferenceSlider, "text.ageDifference");
        languageController.bindComponent(textGenderDifferenceSlider, "text.genderDifference");
        languageController.bindComponent(textFoodPreferenceSlider, "text.foodPreference");
        // The language of default Buttons gets set to the current default language when initialized.
        // LanguageController always changes the default language to get around this.
        // This also means that default Buttons can't change the language dynamically.
    }

    public void init(Window owner) {
        try {
            String relPath = "src/main/java/view/fxml/pairingWeights.fxml";
            File file = new File(relPath);
            String absPath = file.getAbsolutePath();
            URL url = new URL("file:///" + absPath);
            FXMLLoader loader = new FXMLLoader(url);

            loader.setController(this); // Do not delete!!! It does not work in any other way!
            DialogPane pane = loader.load();

            pane.getButtonTypes().addAll(ButtonType.CLOSE, ButtonType.APPLY);

            setResultConverter(buttonType -> {
                if(!Objects.equals(ButtonBar.ButtonData.APPLY, buttonType.getButtonData())) {
                    return null;
                }

                return getPairingWeights();
            });

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setTitle(languageController.getText("root.dialog.pairingWeights"));
            setDialogPane(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PairingWeights getPairingWeights() {
        return new PairingWeights(
                ageDiffWeightSliderPair.getValue(),
                genderDiffWeightSliderPair.getValue(),
                foodPrefWeightSliderPair.getValue()
        );
    }
}
