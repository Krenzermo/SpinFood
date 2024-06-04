package model.event.list.identNumbers;

import model.event.InputData;
import model.event.PairingWeights;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairIdentNumberTest {

    static ParticipantCollectionList pairList1;
    static ParticipantCollectionList pairList2;

    @BeforeAll
    static void setUp() {
        InputData inputData = InputData.getInstance();
        PairingWeights pairingWeights1 = new PairingWeights(1, 1, 1);
        PairingWeights pairingWeights2 = new PairingWeights(7, 3, 5);

        pairList1 = new PairList(inputData, pairingWeights1);
        pairList2 = new PairList(inputData, pairingWeights2);
    }

    @Test
    void calcGenderDiversity1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        double delta = .05;
        Assertions.assertEquals(.22, num1.genderDiversity, delta);
    }

    @Test
    void calcGenderDiversity2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        double delta = .05;
        Assertions.assertEquals(.25, num2.genderDiversity, delta);
    }

    @Test
    void calcAgeDifference() {
    }

    @Test
    void calcPreferenceDeviation() {
    }
}