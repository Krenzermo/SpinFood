package model.processing.states;

import model.event.collection.Pair;
import model.event.list.GroupList;
import model.event.list.PairList;
import model.event.list.weight.GroupWeights;
import model.event.list.weight.Weights;

public class State {

    private State prev;
    private PairList pairList;
    private GroupList groupList;

    public State(PairList pairList, GroupList groupList) {
        prev = null;
        this.pairList = pairList;
        this.groupList = groupList;
    }

    public void updateState(PairList pairList, GroupList groupList) {
        if (pairList == null) {
            assert groupList == null;
            return;
        }

        prev = new State(this.pairList, this.groupList);

        this.groupList = new GroupList(groupList);
        this.pairList = this.groupList.getPairList();

    }

    public void updateState(PairList pairList, Weights weights) {
        prev = new State(this.pairList, this.groupList);

        this.groupList = new GroupList(pairList, weights);
        this.pairList = groupList.getPairList();
    }


    public State revertState(Weights weights) {
        var gl = new GroupList(prev.pairList, weights);
        var pl = gl.getPairList();

        var gl2 = new GroupList(pairList, weights);
        prev = new State(gl2.getPairList(), gl2);

        pairList = pl;
        groupList = gl;

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
