package model.processing.states;

/**
 * Abstract class representing a state in a state machine or processing workflow.
 */
public abstract class State {

    protected State prev; // Reference to the previous state

    // Constructor is not shown because it's implicit in abstract classes

    /**
     * Resets the state to the previous state.
     *
     * @return The previous state object.
     */
    public abstract State reset();

}
