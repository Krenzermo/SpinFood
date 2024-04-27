package model;

public class InputData {
	private static InputData instance;

	private Location eventLocation;

	private InputData() {
		//TODO: this
		// wird von Ole erstellt
	}

	public static InputData getInstance() {
		if (instance == null) {
			instance = new InputData();
		}
		return instance;
	}

	public Location getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(Location eventLocation) {
		this.eventLocation = eventLocation;
	}
}
