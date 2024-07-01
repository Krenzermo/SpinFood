package controller.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.event.io.CancellationHandler;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;

public class UnsubscriberController {

    @FXML
    private Label labelParticipant;

    @FXML
    private ComboBox<Participant> comboBoxSuccessor;

    private Participant participant;
    private PairList pairList;
    private GroupList groupList;
    private PairingWeights pairingWeights;
    private GroupWeights groupWeights;

    @FXML
    private void initialize() {
        // Any required initialization can be done here
    }

    public void initData(Participant participant, PairList pairList, GroupList groupList, PairingWeights pairingWeights, GroupWeights groupWeights) {
        this.participant = participant;
        this.pairList = pairList;
        this.groupList = groupList;
        this.pairingWeights = pairingWeights;
        this.groupWeights = groupWeights;

        labelParticipant.setText("Teilnehmer: " + participant.getName());
        comboBoxSuccessor.getItems().addAll(getSuccessorList());
    }

    private List<Participant> getSuccessorList() {
        return new ArrayList<>(pairList.getSuccessors());
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(participant);

        CancellationHandler cancellationHandler = new CancellationHandler(pairList, groupList);
        cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);

        closeWindow();
    }

    @FXML
    private void handleSubstitute(ActionEvent event) {
        Participant successor = comboBoxSuccessor.getValue();
        if (successor != null) {
            replaceParticipant(participant, successor);
            closeWindow();
        }
    }

    private void replaceParticipant(Participant oldParticipant, Participant newParticipant) {
        for (Pair pair : pairList) {
            if (pair.getParticipants().contains(oldParticipant)) {
                if (pair.getParticipants().get(0).equals(oldParticipant)) {
                    pair.getParticipants().set(0, newParticipant);
                } else {
                    pair.getParticipants().set(1, newParticipant);
                }
                break;
            }
        }

        for (Group group : groupList) {
            for (Pair pair : group.getPairs()) {
                if (pair.getParticipants().contains(oldParticipant)) {
                    if (pair.getParticipants().get(0).equals(oldParticipant)) {
                        pair.getParticipants().set(0, newParticipant);
                    } else {
                        pair.getParticipants().set(1, newParticipant);
                    }
                    break;
                }
            }
        }

        pairList.getSuccessors().remove(newParticipant);
        pairList.getSuccessors().add(oldParticipant);
    }

    private void closeWindow() {
        Stage stage = (Stage) labelParticipant.getScene().getWindow();
        stage.close();
    }
}
