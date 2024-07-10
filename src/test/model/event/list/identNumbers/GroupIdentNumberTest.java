package model.event.list.identNumbers;

import model.event.list.weight.GroupWeights;
import model.event.io.InputData;
import model.event.list.weight.PairingWeights;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.ParticipantCollectionList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the GroupIdentNumber class.
 *
 * Tests various calculations related to groupList identification numbers.
 * Tests only if the values are in a reasonable area, as testing on possibly changing real data
 * does not allow precise tests.
 */
public class GroupIdentNumberTest {

    /**
     * Sets up the test data before all tests run.
     */
    static ParticipantCollectionList GroupList1;
    static ParticipantCollectionList pairList;
    @BeforeAll
    static void setUp() {
        InputData inputData = InputData.getInstanceDebug();
        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        pairList = new PairList(pairingWeights);
        GroupWeights groupPairingWeights = new GroupWeights(1, 1, 1, 1);

        GroupList1 = new GroupList((PairList) pairList, groupPairingWeights);


    }
    /**
     * Tests if the genderDiversity Identnumber is between 0 and 0.5
     */
    @Test
    void calcGenderDiversity() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        double delta = .25;
        Assertions.assertEquals(.25, num.genderDiversity, delta);
    }
    /**
     * Tests if the age Identnumber is at least 0
     */
    @Test
    void calcAgeDifference() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.ageDifference >= 0);
    }
    /**
     * Tests if the foodPref Identnumber is at least 0 and at most 2
     */
    @Test
    void calcPreferenceDeviation() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.preferenceDeviation >= 0 && num.preferenceDeviation <=2);
    }
    /**
     * Tests if the averagePath Identnumber is above 0
     */
    @Test
    void calcAveragePathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getAveragePathLength() > 0);
    }
    /**
     * Tests if the totalPath Identnumber is above 0
     */
    @Test
    void calcTotalPathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getTotalPathLength() > 0);
    }
    /**
     * Tests if the pathDeviation Identnumber is above 0
     */
    @Test
    void calcStandardDeviationPathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getPathLengthStdDev() > 0);
    }
    /**
     * Tests if the numElements Identnumber is at least 0
     */
    @Test
    void numElements() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.numElems >= 0);
    }
    /**
     * Tests if the numSuccessors Identnumber is at least 0
     */
    @Test
    void numSuccessors() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.numSuccessors >= 0);
    }
}
