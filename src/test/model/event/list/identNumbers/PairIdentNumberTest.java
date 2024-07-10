package model.event.list.identNumbers;

import model.event.collection.Pair;
import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PairIdentNumber class.
 *
 * Tests various calculations related to pairList identification numbers.
 * Tests only if the values are in a reasonable area, as testing on possibly changing real data
 * does not allow precise tests.
 */
class PairIdentNumberTest {

    static ParticipantCollectionList<Pair> pairList1;
    static ParticipantCollectionList<Pair> pairList2;

    /**
     * Sets up the test data before all tests run.
     */
    @BeforeAll
    static void setUp() {
        InputData inputData = InputData.getInstanceDebug();
        PairingWeights pairingWeights1 = new PairingWeights(1, 1, 1);
        PairingWeights pairingWeights2 = new PairingWeights(7, 3, 5);

        pairList1 = new PairList(pairingWeights1);
        pairList2 = new PairList(pairingWeights2);
    }

    /**
     * Tests if the genderDiversity Identnumber is between 0 and 0.5
     */
    @Test
    void calcGenderDiversity1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        double delta = .25;
        Assertions.assertEquals(.25, num1.genderDiversity, delta);
    }

    /**
     * Tests if the genderDiversity Identnumber is between 0 and 0.5 with a different set of pairingWeights
     */
    @Test
    void calcGenderDiversity2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        double delta = .25;
        Assertions.assertEquals(.25, num2.genderDiversity, delta);
    }

    /**
     * Tests if the age Identnumber between 0 and 8
     */
    @Test
    void calcAgeDifference1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);

        Assertions.assertTrue(num1.ageDifference >=0 && num1.ageDifference <=8);
    }
    /**
     * Tests if the age Identnumber is between 0 and 8 with a different set of pairingWeights
     */
    @Test
    void calcAgeDifference2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        Assertions.assertTrue(num2.ageDifference >=0 && num2.ageDifference <=8);
    }
    /**
     * Tests if the foodPref Identnumber is at least 0 and at most 2
     */
    @Test
    void calcPreferenceDeviation1() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue(num1.preferenceDeviation >= 0 && num1.preferenceDeviation <= 2);
    }
    /**
     * Tests if the foodPref Identnumber is at least 0 and at most 2 with a different set of pairingWeights
     */
    @Test
    void calcPreferenceDeviation2() {
        PairIdentNumber num2 = new PairIdentNumber((PairList) pairList2);
        Assertions.assertTrue(num2.preferenceDeviation >= 0 && num2.preferenceDeviation <= 2);
    }
    /**
     * Tests if the numElements Identnumber is at least 0
     */
    @Test
    void numElements() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue( num1.numElems >= 0);
    }
    /**
     * Tests if the numSuccessors Identnumber is at least 0
     */
    @Test
    void numSuccessors() {
        PairIdentNumber num1 = new PairIdentNumber((PairList) pairList1);
        Assertions.assertTrue( num1.numSuccessors >= 0);
    }
}
