package controller.FXMLControllers;

import controller.LanguageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
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

public class UnsubscriberController extends Dialog<PairList> {
    private static final LanguageController languageController = LanguageController.getInstance();

    @FXML
    private Label labelParticipant;

    @FXML
    private ComboBox<Participant> comboBoxSuccessor;

    @FXML
    private Button signOutAndReplace;

    @FXML
    private Button SignOut;

    @FXML
    private Button SignPairOut;

    private Participant participant;
    private PairList pairList;
    private GroupList groupList;

    @FXML
    private void initialize() {
        bindAllComponents();
    }

    private void bindAllComponents() {
        // TODO: this
    }

    public void initData(Participant participant, PairList pairList, GroupList groupList, Window owner) {
        this.participant = participant;
        this.pairList = pairList;
        this.groupList = groupList;

        labelParticipant.setText("Teilnehmer: " + participant.getName());
        comboBoxSuccessor.getItems().addAll(getSuccessorList());

        // this does nothing as it is not possible to retrieve the data the way this is initialized
        // See i.e., dialog window pairingWeightsController
        setResultConverter(buttonType -> {
            if (!buttonType.equals(ButtonType.CANCEL)) {
                return getPairList();
            }
            return null;
        });

        initOwner(owner);
        setResizable(false);
        setTitle("Abmelden");
    }

    private List<Participant> getSuccessorList() {
        return new ArrayList<>(pairList.getSuccessors());
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        List<Participant> cancelledParticipants = new ArrayList<>();
        cancelledParticipants.add(participant);

        CancellationHandler cancellationHandler = new CancellationHandler(pairList, groupList);
        cancellationHandler.handleCancellation(cancelledParticipants);

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

    @FXML
    private void handleLogOutAsPair(ActionEvent event) {
        Pair affectedPair = participant.getPair();
        if (affectedPair != null) {
            Participant partner = affectedPair.getOtherParticipant(participant);

            List<Participant> cancelledParticipants = new ArrayList<>();
            cancelledParticipants.add(participant);
            if (partner != null) {
                cancelledParticipants.add(partner);
            }

            CancellationHandler cancellationHandler = new CancellationHandler(pairList, groupList);
            cancellationHandler.handleCancellation(cancelledParticipants);

            closeWindow();
        }
    }

    private void replaceParticipant(Participant oldParticipant, Participant newParticipant) {
        pairList.getSuccessors().remove(newParticipant);
        oldParticipant.getPair().replaceParticipant(oldParticipant, newParticipant);
    }

    private void closeWindow() {
        Stage stage = (Stage) labelParticipant.getScene().getWindow();
        stage.close();
    }

    private PairList getPairList() {
        return pairList;
    }
}
