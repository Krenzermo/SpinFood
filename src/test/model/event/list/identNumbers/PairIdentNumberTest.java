package model.event.list.identNumbers;

import model.event.io.InputData;
import model.event.weight.PairingWeights;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        double delta = .5;
        Assertions.assertEquals(.5, num1.genderDiversity, delta);
    }

    @Test
    void calcGenderDiversity2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        double delta = .5;
        Assertions.assertEquals(.5, num2.genderDiversity, delta);
    }

    @Test
    void calcAgeDifference1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);

        Assertions.assertTrue(num1.ageDifference >=0 && num1.ageDifference <=8);
    }

    @Test
    void calcAgeDifference2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        Assertions.assertTrue(num2.ageDifference >=0 && num2.ageDifference <=8);
    }

    @Test
    void calcPreferenceDeviation1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue(num1.preferenceDeviation >= 0 && num1.preferenceDeviation <= 2);
    }

    @Test
    void calcPreferenceDeviation2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        Assertions.assertTrue(num2.preferenceDeviation >= 0 && num2.preferenceDeviation <= 2);
    }

    @Test
    void numElements() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue( num1.numElems >= 0);
    }

    @Test
    void numSuccessors() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue( num1.numSuccessors >= 0);
    }
}