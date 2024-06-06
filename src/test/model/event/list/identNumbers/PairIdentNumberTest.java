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
        Assertions.assertEquals(.246, num2.genderDiversity, delta);
    }

    @Test
    void calcAgeDifference1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        double delta = .005;
        Assertions.assertEquals(.53, num1.ageDifference, delta);
    }

    @Test
    void calcAgeDifference2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        double delta = .005;
        Assertions.assertEquals(.506, num2.ageDifference, delta);
    }

    @Test
    void calcPreferenceDeviation1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        double delta = .005;
        Assertions.assertEquals(.598, num1.preferenceDeviation, delta);
    }

    @Test
    void calcPreferenceDeviation2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        double delta = .005;
        Assertions.assertEquals(.60, num2.preferenceDeviation, delta);
    }

    @Test
    void numElements() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertEquals(152, num1.numElems);
    }

    @Test
    void numSuccessors() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertEquals(1, num1.numSuccessors);
    }
}