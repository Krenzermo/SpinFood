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

    public void updateState(PairList pairList, GroupList groupList) {
        if (pairList == null && groupList == null) {
            return;
        }

        assert pairList != null;
        var pl = new PairList(pairList);
        GroupList gl = null;
        if (this.pairList == null) {
            prev = new State(null, null);
        } else if (groupList == null) {
            prev = new State(new PairList(this.pairList), null);
        } else {
            gl = new GroupList(groupList);
            prev = new State(new PairList(this.pairList), new GroupList(this.groupList));
        }
        this.pairList = pl;
        this.groupList = gl;
    }

    public State revertState() {
        var pl = new PairList(prev.pairList);
        GroupList gl = null;
        if (prev.groupList != null) {
            gl = new GroupList(prev.groupList);
        }

        if (groupList != null) {
            prev = new State(new PairList(pairList), new GroupList(groupList));
        } else {
            prev = new State(new PairList(pairList), null);
        }

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
