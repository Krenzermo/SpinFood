package model.processing.states;

import model.event.list.GroupList;
import model.event.list.PairList;

import java.util.Objects;

public class State {
    private State prev;
    private PairList pairList;
    private GroupList groupList;

    private static State mainState;

    public static synchronized State getInstance() {
        if (mainState == null) {
            mainState = new State();
        }
        return mainState;
    }

    private State() {
        mainState = this;
        prev = null;
        pairList = null;
        groupList = null;
    }

    private State(State prev) {
        this.prev = prev;
        pairList = prev.getPairList();
        groupList = prev.getGroupList();
    }

    public void updateState(PairList pairList, GroupList groupList) {
        if (pairList == null) {
            assert groupList == null;
            return;
        }

        prev = new State(this);

        if (groupList == null) {
            this.pairList = new PairList(pairList);
            this.groupList = null;
        } else {
            this.groupList = new GroupList(groupList);
            this.pairList = this.groupList.getPairList();
        }
    }

    public State revertState() {
        pairList = prev.getPairList();
        groupList = prev.getGroupList();

        prev = null;

        return this;
    }

    public PairList getPairList() {
        return pairList;
    }

    public GroupList getGroupList() {
        return groupList;
    }

    public State getPrev() {
        return prev;
    }
}
