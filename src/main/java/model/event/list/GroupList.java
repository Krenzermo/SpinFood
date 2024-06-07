package model.event.list;

import model.event.collection.Group;
import model.event.collection.ParticipantCollection;
import model.event.list.identNumbers.IdentNumber;
import model.person.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * The GroupList class represents a collection of Groups of Pairs.
 * It provides functionality to group participants based on various criteria
 * and maintains a list of unpaired participants (successors).
 *
 * @author Daniel Hinkelmann
 * @author Finn Brecher
 */
public class GroupList extends ParticipantCollectionList<Group> {
	private IdentNumber identNumber;
	private static final List<Participant> successors = new ArrayList<>();

	public GroupList() {
		setList(buildGroups());
	}

	private static List<Group> buildGroups() {
		//TODO
		return new ArrayList<>();
	}

	/**
	 * Gets the identifying number for this GroupList.
	 *
	 * @return the {@link IdentNumber} (Identifying Numbers) of this GroupList
	 */
	@Override
	public IdentNumber getIdentNumber() {
		// TODO: this
		return null;
	}

	/**
	 *  Evaluates the GroupList.
	 *
	 * @return the evaluation of this GroupList
	 */
	@Override
	public double evaluate() {
		// TODO: this
		return 0;
	}

	public List<Group> getGroups() {
		return this;
	}

	public List<Participant> getSuccessors() {
		return successors;
	}
}
