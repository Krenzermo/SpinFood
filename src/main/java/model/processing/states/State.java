package model.processing.states;

import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.Weights;

import java.util.Objects;

public class State {
    private State prev;
    private PairList pairList;
    private GroupList groupList;

    public void init(PairList pairList, GroupList groupList) {
        if (groupList != null) {
            this.groupList = new GroupList(groupList);
            this.pairList = new PairList(pairList);
        } else {
            this.pairList = new PairList(pairList);
        }
    }

    public State(GroupList groupList, PairList pairList) {
        this.pairList = pairList;
        this.groupList = groupList;
    }

    private State() {
        prev = null;
        pairList = null;
        groupList = null;
    }

    private State(State prev) {
        this.prev = prev;
        if (prev.groupList != null) {
            groupList = new GroupList(prev.getGroupList());
            pairList = groupList.getPairList();
        } else if (prev.pairList != null) {
            pairList = new PairList(prev.getPairList());
        }
    }

    public void updateState(PairList pairList, GroupList groupList) {
        if (pairList == null) {
            assert groupList == null;
            return;
        }

        prev = new State(this);
        prev.prev = null;

        if (groupList != null) {
            this.groupList = new GroupList(groupList);
            this.pairList = this.groupList.getPairList();
        } else {
            this.pairList = new PairList(pairList);
        }
    }

    public State revertState() {
        this.pairList = prev.pairList;
        this.groupList = prev.groupList;
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
