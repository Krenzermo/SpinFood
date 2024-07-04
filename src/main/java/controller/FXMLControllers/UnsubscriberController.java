package controller.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.event.collection.Group;
import model.event.collection.Pair;
import model.processing.CancellationHandler;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.PairingWeights;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;

public class UnsubscriberController extends Dialog<ParticipantCollectionList> {

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

    }

    public void initData(Participant participant, PairList pairList, GroupList groupList, PairingWeights pairingWeights, GroupWeights groupWeights, Window owner) {
        this.participant = participant;
        this.pairList = pairList;
        this.groupList = groupList;
        this.pairingWeights = pairingWeights;
        this.groupWeights = groupWeights;

        labelParticipant.setText("Teilnehmer: " + participant.getName());
        comboBoxSuccessor.getItems().addAll(getSuccessorList());

        initOwner(owner);
        setResizable(false);
        setTitle("Abmelden");
    }

    private List<Participant> getSuccessorList() {
        return new ArrayList<>(pairList.getSuccessors());
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        closeWindow();
        new Thread(() -> {
            List<Participant> cancelledParticipants = new ArrayList<>();
            cancelledParticipants.add(participant);

            CancellationHandler cancellationHandler = new CancellationHandler(pairList, groupList);
            cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);
        }).start();
    }

    @FXML
    private void handleSubstitute(ActionEvent event) {
        Participant successor = comboBoxSuccessor.getValue();
        if (successor != null) {
            closeWindow();
            new Thread(() -> {
                replaceParticipant(participant, successor);
            }).start();
        }
    }

    @FXML
    private void handleLogOutAsPair(ActionEvent event) {
        Pair affectedPair = findAffectedPair(participant);
        if (affectedPair != null) {
            Participant partner = findPartner(affectedPair, participant);

            closeWindow();
            new Thread(() -> {
                List<Participant> cancelledParticipants = new ArrayList<>();
                cancelledParticipants.add(participant);
                if (partner != null) {
                    cancelledParticipants.add(partner);
                }

                CancellationHandler cancellationHandler = new CancellationHandler(pairList, groupList);
                cancellationHandler.handleCancellation(cancelledParticipants, pairingWeights, groupWeights);
            }).start();
        }
    }

    private Pair findAffectedPair(Participant participant) {
        for (Pair pair : pairList) {
            if (pair.getParticipants().contains(participant)) {
                return pair;
            }
        }
        return null;
    }

    private Participant findPartner(Pair pair, Participant participant) {
        return pair.getParticipants().stream()
                .filter(p -> !p.equals(participant))
                .findFirst()
                .orElse(null);
    }

    private void replaceParticipant(Participant oldParticipant, Participant newParticipant) {
        pairList.getSuccessors().remove(newParticipant);

        for (Pair pair : pairList) {
            if (pair.getParticipants().contains(oldParticipant)) {
                pair.replaceParticipant(oldParticipant, newParticipant);
            }
        }

        for (Group group : groupList) {
            for (Pair pair : group.getPairs()) {
                if (pair.getParticipants().contains(oldParticipant)) {
                    pair.replaceParticipant(oldParticipant, newParticipant);
                }
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) labelParticipant.getScene().getWindow();
        stage.close();
    }
}
