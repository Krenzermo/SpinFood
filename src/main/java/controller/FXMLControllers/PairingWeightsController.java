package controller.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import model.event.collection.Pair;
import model.event.list.weight.PairingWeights;
import javafx.scene.layout.AnchorPane;

public class PairingWeightsController {

    public static volatile ButtonFlag acceptButtonFlag = new ButtonFlag();
    public static Stage stage;

    @FXML
    private AnchorPane pairingWeightsPane;

    @FXML
    Button acceptPairingWeightsButton;

    @FXML
    private Slider ageDiffWeightSliderPair;

    @FXML
    private Slider fooPrefWeightSliderPair;

    @FXML
    private Slider genDivWeightSliderPair;

    @FXML
    void pairingWeiightsAccetpted(ActionEvent event) {

        PairingWeights pairingWeights = new PairingWeights(
                ageDiffWeightSliderPair.getValue(),
                fooPrefWeightSliderPair.getValue(),
                genDivWeightSliderPair.getValue()
        );
        acceptButtonFlag.pairingWeights = pairingWeights;
        acceptButtonFlag.flag = true;

        stage.close();
        ageDiffWeightSliderPair.setValue(1);
        fooPrefWeightSliderPair.setValue(1);
        genDivWeightSliderPair.setValue(1);

    }

    public static class ButtonFlag {
        boolean flag;
        PairingWeights pairingWeights;

        public ButtonFlag() {
            flag = false;
        }
    }

}
