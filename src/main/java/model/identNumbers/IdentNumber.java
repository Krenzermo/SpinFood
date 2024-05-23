package model.identNumbers;

/**
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 */
public abstract class IdentNumber {
	//TODO: this
    private int numElems;
    private int numSuccessors;
    private int genderDiversity;
    private int ageDifference;
    private int preferenceDeviation;

    protected IdentNumber(int numElems, int numSuccessors, int genderDiversity, int ageDifference, int preferenceDeviation) {
        this.numElems = numElems;
        this.numSuccessors = numSuccessors;
        this.genderDiversity = genderDiversity;
        this.ageDifference = ageDifference;
        this.preferenceDeviation = preferenceDeviation;
    }


}
