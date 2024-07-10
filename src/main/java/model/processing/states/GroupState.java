package model.processing.states;

import model.event.list.GroupList;

/**
 * Represents the state of a group list in a processing workflow.
 * Extends the State class.
 */
public class GroupState extends State{

    private GroupList groupList;

    /**
     * Constructs a GroupState object with the specified group list.
     *
     * @param groupList The group list to associate with this state.
     */
    public GroupState(GroupList groupList) {
        this.groupList = groupList;
        this.prev = this;
    }

    /**
     * Updates the group list associated with this state.
     *
     * @param groupList The new group list to update.
     * @return The updated state object.
     */
    public State update(GroupList groupList) {
        prev = this;
        this.groupList = groupList;
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