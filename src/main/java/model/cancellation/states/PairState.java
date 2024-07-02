package model.cancellation.states;

import model.event.list.GroupList;
import model.event.list.PairList;

public class PairState extends State{

    private PairList pairList;

    public PairState(PairList pairList) {
        this.pairList = pairList;
        this.prev = this;
    }

    public State update(PairList pairList) {
        prev = this;
        this.pairList = pairList;
        return this;
    }

    public State reset() {
        return prev;
    }
}
