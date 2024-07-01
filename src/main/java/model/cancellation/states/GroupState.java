package model.cancellation.states;

import model.event.list.GroupList;

public class GroupState extends State{

    private GroupList groupList;

    public GroupState(GroupList groupList) {
        this.groupList = groupList;
        this.prev = this;
    }

    public State update(GroupList groupList) {
        prev = this;
        this.groupList = groupList;
        return this;
    }

    public State reset() {
        return prev;
    }

}
