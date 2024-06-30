package controller.FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;
import model.event.list.weight.GroupWeights;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GroupWeightsController extends Dialog<GroupWeights> {
	@FXML
	private DialogPane root;

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
		ageDiffWeightSliderGroup.setValue(1.0);
		genderDiffWeightSliderGroup.setValue(1.0);
		foodPrefWeightSliderGroup.setValue(1.0);
		distanceWeightSliderGroup.setValue(1.0);
	}

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
