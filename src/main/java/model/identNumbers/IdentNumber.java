package model.identNumbers;

/**
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 */
public class IdentNumber {
	//TODO: this
    private int numElems;
    private int numSuccessors;
    private double genderDiversity;
    private double ageDifference;
    private double preferenceDeviation;

    public IdentNumber(int numElems, int numSuccessors, double genderDiversity, double ageDifference, double preferenceDeviation) {
        this.numElems = numElems;
        this.numSuccessors = numSuccessors;
        this.genderDiversity = genderDiversity;
        this.ageDifference = ageDifference;
        this.preferenceDeviation = preferenceDeviation;
    }

    public String toString() {
        return "IdentNumber {" +
                "\nnumElems=" + numElems +
                "\nnumSuccessors=" + numSuccessors +
                "\ngenderDiversity(higher=better)=" + genderDiversity +
                "\nageDifference(lower=better)=" + ageDifference +
                "\npreferenceDeviation(lower=better)=" + preferenceDeviation +
                "\n}";
    }


}
