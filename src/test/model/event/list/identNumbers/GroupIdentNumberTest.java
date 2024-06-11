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

public class GroupIdentNumberTest {

    static ParticipantCollectionList GroupList1;
    static ParticipantCollectionList pairList;
    @BeforeAll
    static void setUp() {
        InputData inputData = InputData.getInstance();
        PairingWeights pairingWeights = new PairingWeights(1, 1, 1);
        pairList = new PairList(inputData, pairingWeights);
        GroupWeights groupPairingWeights = new GroupWeights(1, 1, 1, 1);

        GroupList1 = new GroupList((PairList) pairList, groupPairingWeights);


    }

    @Test
    void calcGenderDiversity() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        double delta = .5;
        Assertions.assertEquals(.5, num.genderDiversity, delta);
    }

    @Test
    void calcAgeDifference() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.ageDifference >= 0);
    }

    @Test
    void calcPreferenceDeviation() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.preferenceDeviation >= 0 && num.preferenceDeviation <=2);
    }
    @Test
    void calcAveragePathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getAveragePathLength() > 0);
    }
    @Test
    void calcTotalPathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getTotalPathLength() > 0);
    }
    @Test
    void calcStandardDeviationPathLength() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.getPathLengthStdDev() > 0);
    }
    @Test
    void numElements() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.numElems >= 0);
    }
    @Test
    void numSuccessors() {
        GroupIdentNumber num = new GroupIdentNumber((GroupList) GroupList1);
        Assertions.assertTrue(num.numSuccessors >= 0);
    }
}
