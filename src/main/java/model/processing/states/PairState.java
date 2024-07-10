package model.processing.states;

import model.event.list.PairList;

/**
 * Represents the state of a pair list in a processing workflow.
 * Extends the State class.
 */
public class PairState extends State {

    private PairList pairList;

    /**
     * Constructs a PairState object with the specified pair list.
     *
     * @param pairList The pair list to associate with this state.
     */
    public PairState(PairList pairList) {
        this.pairList = pairList;
        this.prev = this;
    }

    /**
     * Updates the pair list associated with this state.
     *
     * @param pairList The new pair list to update.
     * @return The updated state object.
     */
    public State update(PairList pairList) {
        prev = this;
        this.pairList = pairList;
        return this;
    }

    /**
     * Resets the state to the previous state.
     *
     * @return The previous state object.
     */
    public State reset() {
        return prev;
    }
}
