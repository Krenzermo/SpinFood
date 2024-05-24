package model.event.list;

import model.event.collection.Pair;
import model.event.collection.ParticipantCollection;
import model.identNumbers.IdentNumber;

import java.util.ArrayList;
import java.util.List;

public class PairList implements ParticipantCollectionList {
    private IdentNumber identNumber;
    private final List<ParticipantCollection> pairs;

    public PairList() {
        this.pairs = new ArrayList<>();
    }

    /**
     * @return the {@link IdentNumber} (Identifying Numbers) of this ParticipantCollectionList
     */
    @Override
    public IdentNumber getIdentNumber() {
        // TODO: this
        return null;
    }

    /**
     * @return the evaluation of this ParticipantCollection
     */
    @Override
    public double evaluate() {
        // TODO: this
        return 0;
    }

    /**
     * This method is used to change the underlying data structure.
     * It is used by the default implementations of {@link #add}, {@link #remove}, {@link #addUnsafe},
     * {@link #addAll} and {@link #removeAll}methods.
     *
     * @return the data structure that stores the instances of {@link ParticipantCollection}
     */
    @Override
    public List<ParticipantCollection> getDataStructure() {
        return pairs;
    }

    /**
     * Checks if the collection has the same type as this ParticipantCollectionList (subclass)
     *
     * @param collection the element to be checked
     * @throws IllegalArgumentException if the type check fails
     */
    @Override
    public void checkType(ParticipantCollection collection) {
        if (!collection.getClass().equals(Pair.class)) {
            throw new IllegalArgumentException("Collection is not a Pair");
        }
    }
}
