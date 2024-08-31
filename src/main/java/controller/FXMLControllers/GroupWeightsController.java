package controller.FXMLControllers;

import controller.LanguageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Window;
import model.event.list.weight.GroupWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * GroupWeightsController handels the logistics of changing the groupWeights for creating a groupList
 */
public class GroupWeightsController extends Dialog<GroupWeights> {
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
	private Text textDistanceSlider;

	@FXML
	private Slider ageDiffWeightSliderGroup;

	@FXML
	private Slider genderDiffWeightSliderGroup;

	@FXML
	private Slider foodPrefWeightSliderGroup;

	@FXML
	private Slider distanceWeightSliderGroup;

	@FXML
	private void initialize() {
		bindAllComponents();

		ageDiffWeightSliderGroup.setValue(1.0);
		genderDiffWeightSliderGroup.setValue(1.0);
		foodPrefWeightSliderGroup.setValue(1.0);
		distanceWeightSliderGroup.setValue(1.0);
	}

	private void bindAllComponents() {
		// TODO: this
	}

	/**
	 * method to initialize the groupWeight controller window
	 * @param owner caller of this window
	 */
	public void init(Window owner) {
		try {
			String relPath = "src/main/java/view/fxml/groupWeights.fxml";
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

				return getGroupWeights();
			});

			initOwner(owner);
			initModality(Modality.APPLICATION_MODAL);
			setResizable(false);
			setTitle("Gruppen Parameter einstellen");
			setDialogPane(pane);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private GroupWeights getGroupWeights() {
		return new GroupWeights(
				ageDiffWeightSliderGroup.getValue(),
				genderDiffWeightSliderGroup.getValue(),
				foodPrefWeightSliderGroup.getValue(),
				distanceWeightSliderGroup.getValue()
		);
	}
}
