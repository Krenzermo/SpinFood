package model.processing.states;

import model.event.list.GroupList;
import model.event.list.PairList;

public class State {

    private State prev;
    private PairList pairList;
    private GroupList groupList;

    public State(PairList pairList, GroupList groupList) {
        prev = null;
        this.pairList = pairList;
        this.groupList = groupList;
    }

    public State updateState(PairList pairList, GroupList groupList) {
        prev = new State(this.pairList, this.groupList); //TODO: Use copy constructor
        this.pairList = pairList;
        this.groupList = groupList;
        return this;
    }

    public State revertState() {
        return prev;
    }

    public PairList getPairList() {
        return pairList;
    }

    public GroupList getGroupList() {
        return groupList;
    }
}
