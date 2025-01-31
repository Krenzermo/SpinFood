package model.event.io;

import model.event.Location;
import model.event.collection.Pair;
import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Gender;
import model.person.Name;
import model.person.Participant;
import model.kitchen.Kitchen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.nio.file.Paths;

/**
 * @author Ole Krenzer
 * @author Finn Brecher
 * @author Daniel Hinkelmann
 * @author Davide Piacenza
 *
 * class to save an event location from an external file and save participants and pairs from another external file
 * in the corresponding classes in ArrayLists to return them all together
 */
public class InputData {
    private ArrayList<Participant> participantInputData;
    private ArrayList<Pair> pairInputData;
    private ArrayList<Participant> participantSuccessorList; // List to store participants with overused kitchens
    private ArrayList<Pair> pairSuccessorList; // List to store pairs with overused kitchens
    private Location eventLocation;
    // TODO: add more robust logic for receiving the data
    private String participantDataFilePath;
    private String eventLocationDataFilePath;
    private static final String participantsPathDebug = "src/main/java/data/teilnehmerliste.csv";
    private static final String eventLocationPathDebug = "src/main/java/data/partylocation.csv";

    private Map<Kitchen, Integer> kitchenCountMap; // Map to count kitchen usage

    private static InputData inputData; // Singleton

    private int idCounter = 0;

    /**
     * constructor for InputData
     */
    /*
    private InputData(String participantDataFilePath, String eventLocationDataFilePath) {
        inputData = this;
        this.participantInputData = new ArrayList<>();
        this.pairInputData = new ArrayList<>();
        this.participantSuccessorList = new ArrayList<>();
        this.pairSuccessorList = new ArrayList<>();
        this.kitchenCountMap = new HashMap<>();
        this.eventLocationDataFilePath = eventLocationDataFilePath;
        this.participantDataFilePath = participantDataFilePath;
        saveLocation();
        saveParticipants();
    }

     */

    /**
     * Private constructor for Singleton pattern.
     * Initializes lists and maps to manage event data.
     */
    private InputData() {
        inputData = this;
        this.participantInputData = null;
        this.pairInputData = null;
        this.participantSuccessorList = null;
        this.pairSuccessorList = null;
        this.kitchenCountMap = null;
        this.eventLocationDataFilePath = null;
        this.participantDataFilePath = null;
        this.eventLocation = null;
    }

    /**
     * Initializes the InputData instance with file paths for participant and event location data.
     *
     * @param participantDataFilePath The file path for participant data.
     * @param eventLocationDataFilePath The file path for event location data.
     */
    public void init(String participantDataFilePath, String eventLocationDataFilePath) {
        initEventLocation(eventLocationDataFilePath);
        initParticipants(participantDataFilePath);
    }

    /**
     * Initializes participant-related data structures and file path.
     *
     * @param participantDataFilePath The file path for participant data.
     */
    public void initParticipants(String participantDataFilePath) {
        this.participantInputData = new ArrayList<>();
        this.participantSuccessorList = new ArrayList<>();
        this.pairInputData = new ArrayList<>();
        this.pairSuccessorList = new ArrayList<>();
        this.kitchenCountMap = new HashMap<>();
        this.participantDataFilePath = participantDataFilePath;
        saveParticipants();

    }

    /**
     *  sets the eventlocation filepath
     * @param eventLocationDataFilePath the path to the eventlocation file
     */

    public void initEventLocation(String eventLocationDataFilePath) {
        this.eventLocationDataFilePath = eventLocationDataFilePath;
        saveLocation();
    }

    private void initDebug() {
        init(participantsPathDebug, eventLocationPathDebug);
    }

    /**
     * @return {@link InputData} instance
     */
    public static synchronized InputData getInstance() {
        if (inputData == null) {
            new InputData();
        }

        return inputData;
    }

    /**
     *
     * @return inputData for debugging and testing
     */
    public static synchronized InputData getInstanceDebug() {
        if (inputData == null) {
            InputData inputData = new InputData();
            inputData.initDebug();
        }
        return inputData;
    }

    /*
    public static synchronized InputData getInstance(String participantDataFilePath, String eventLocationDataFilePath) {
        if (inputData == null) {
            inputData = new InputData(participantDataFilePath, eventLocationDataFilePath);
        }

        return inputData;
    }
     */

    /**
     * method to save the EventLocation
     */
    public void saveLocation() {
        try (Scanner scanner = new Scanner(Paths.get(eventLocationDataFilePath))) {     //build scanner with eventLocation file
            scanner.nextLine();     // skip headline

            String line = scanner.nextLine();   // initialize read line as variable
            String[] parts = line.split(",");   //split the line with ","

            double longitude = Double.parseDouble(parts[0]);
            double latitude = Double.parseDouble(parts[1]);
            eventLocation = new Location(longitude, latitude);        //initialize eventLocation

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to save the Participants from the Participants data file into either the ArrayList for unpaired
     * participants or into the ArrayList for pairs
     */
    public void saveParticipants() {
        try (Scanner scanner = new Scanner(Paths.get(participantDataFilePath))) {
            scanner.nextLine(); // skip headline

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",(?=\\S)|(?<=\\S),"); // split current line by denominator ","

                // extract data from the parts Array into temporary variables to build the participant objects
                Data data = extractData(parts);
                if (data.hasKitchen == KitchenAvailability.NO) {  // Initialize participant without kitchen

                    Participant participant = new Participant(data.id, data.name, data.foodType, data.age, data.gender);
                    participantInputData.add(participant);

                } else if (parts.length < 11) { // Singular participant (signed up alone)

                    Participant participant = new Participant(data.id, data.name, data.foodType, data.age, data.gender, data.hasKitchen, data.kitchenStory, data.kitchenLongitude, data.kitchenLatitude);
                    handleParticipantWithKitchen(participant);

                } else { // Pair (signed up together)

                    Pair pair = getPair(data);
                    handlePairWithKitchen(pair);

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method extracts two Participants from a Data structure and returns them in a Pair
     *
     * @param data The Data of the Participants
     * @return The Pair of the two Participants
     */
    private Pair getPair(Data data) {
        Participant participant1 = new Participant(data.id, data.name, data.foodType, data.age, data.gender, data.hasKitchen, data.kitchenStory, data.kitchenLongitude, data.kitchenLatitude);
        Participant participant2 = new Participant(data.idTwo, data.nameTwo, data.foodType, data.ageTwo, data.genderTwo, data.hasKitchen, data.kitchenStory, data.kitchenLongitude, data.kitchenLatitude);

        Pair pair = new Pair(participant1, participant2, true, idCounter++);
        participant1.setPair(pair);
        participant2.setPair(pair);

        return pair;
    }

    public void setDataFiles(String participantListPath, String locationPath) {
        this.participantDataFilePath = participantListPath;
        this.eventLocationDataFilePath = locationPath;
        kitchenCountMap.clear();
        participantInputData.clear();
        pairInputData.clear();
        pairSuccessorList.clear();
        participantSuccessorList.clear();
        saveLocation();
        saveParticipants();
    }

    /**
     * A structure to shortly hold the data given from the input data document for one participant or pair
     *
     * @param id First Person
     * @param name First person
     * @param foodType First person
     * @param age First person
     * @param gender First person
     * @param hasKitchen First person or both
     * @param kitchenStory First person or both
     * @param kitchenLongitude First person or both
     * @param kitchenLatitude First person or both
     * @param idTwo Second Person
     * @param nameTwo Second Person
     * @param ageTwo Second Person
     * @param genderTwo Second Person
     */
    private record Data(String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen,
                        int kitchenStory, double kitchenLongitude, double kitchenLatitude, String idTwo, Name nameTwo,
                        byte ageTwo, Gender genderTwo) {
        public Data(String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen,
                    int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
            this(id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude,
                    null, null, (byte) 0, null);
        }
    }

    /**
     * This method extracts the Data for the first and second Person and the Kitchen given in the input data document
     *
     * @param parts The split String array of a line from the input data document
     * @return The data in a specific structure
     */
    private Data extractData(String[] parts) {

        //Extract data of Participant 1
        String id = parts[1];
        Name name = new Name(parts[2], "");       //leaving last name empty
        FoodType foodType = FoodType.valueOf(parts[3].toUpperCase());
        byte age = Byte.parseByte(parts[4]);
        Gender gender = Gender.valueOf(parts[5].toUpperCase());
        KitchenAvailability hasKitchen = KitchenAvailability.getAvailability(parts[6]);

        //Extract data of Kitchen Location
        int kitchenStory = parts.length > 7 && !parts[7].isEmpty() ? (int) Double.parseDouble(parts[7]) : 0;
        double kitchenLongitude = parts.length > 8 ? Double.parseDouble(parts[8]) : 0;
        double kitchenLatitude = parts.length > 9 ? Double.parseDouble(parts[9]) : 0;

        //Extract data of possible Participant 2
        if (parts.length < 11) {
            return new Data(id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
        }

        return extractSecData(parts, id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);

    }

    /**
     * This method extracts the data of the second Participant from the input data document
     *
     * @param parts The split String array of a line from the input data document
     * @param id First person
     * @param name First person
     * @param foodType First person
     * @param age First person
     * @param gender First person
     * @param hasKitchen First person
     * @param kitchenStory First person
     * @param kitchenLongitude First person
     * @param kitchenLatitude First person
     * @return The combination of first and second participant data
     */
    private Data extractSecData(String[] parts, String id, Name name, FoodType foodType, byte age, Gender gender, KitchenAvailability hasKitchen, int kitchenStory, double kitchenLongitude, double kitchenLatitude) {
        String idTwo = parts[10];
        Name nameTwo = new Name(parts[11], "");
        byte ageTwo = (byte) Double.parseDouble(parts[12]);      // age of partner is in the input data is stored as double, so we make it a byte again
        Gender genderTwo = Gender.valueOf(parts[13].toUpperCase());

        return new Data(id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude,
                idTwo, nameTwo, ageTwo, genderTwo);
    }

    /**
     * Handles a participant with a kitchen. Adds them to the appropriate list based on kitchen usage.
     *
     * @param participant The participant to handle.
     */
    private void handleParticipantWithKitchen(Participant participant) {
        Kitchen kitchen = participant.getKitchen();
        kitchenCountMap.put(kitchen, kitchenCountMap.getOrDefault(kitchen, 0) + 1);

        if (kitchenCountMap.get(kitchen) > 3) {
            participant.setNoKitchen();
            participantInputData.add(participant);
        } else {
            participantInputData.add(participant);
        }
    }

    /**
     * Handles a pair with a kitchen. Adds them to the appropriate list based on kitchen usage.
     *
     * @param pair The pair to handle.
     */
    private void handlePairWithKitchen(Pair pair) {
        Kitchen kitchen = pair.getKitchen();
        kitchenCountMap.put(kitchen, kitchenCountMap.getOrDefault(kitchen, 0) + 1);

        if (kitchenCountMap.get(kitchen) > 3) {
            pairSuccessorList.add(pair);
        } else {
            pairInputData.add(pair);
        }
    }

    /**
     * method to get the event location
     * @return the Location eventLocation
     */
    public Location getEventLocation() {
        return eventLocation;
    }

    /**
     * method to get the arraylist of the participants from the inputData
     * @return Arraylist of participants
     */
    public ArrayList<Participant> getParticipantInputData() {
        return participantInputData;
    }

    /**
     * method to get the arraylist of the pairs from the inputData
     * @return Arraylist of pairs
     */
    public ArrayList<Pair> getPairInputData() {
        return pairInputData;
    }

    /**
     * method to get the list of participant successors from the inputData
     * @return Arraylist of participant successors
     */
    public ArrayList<Participant> getParticipantSuccessorList() {
        return participantSuccessorList;
    }

    /**
     * method to get the list of pair successors from the inputData
     * @return Arraylist of pair successors
     */
    public ArrayList<Pair> getPairSuccessorList() {
        return pairSuccessorList;
    }

    /**
     * method to get the file path of the participants file.
     * @return a string that contains the file path of the Participants file
     */
    public String getParticipantDataFilePath() {
        return participantDataFilePath;
    }

    /**
     * method to get the file path of the event location file.
     * @return a string that contains the file path of the EventLocation file
     */
    public String getEventLocationDataFilePath() {
        return eventLocationDataFilePath;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string containing information about participant data file path,
     * event location data file path, event location, unpaired participants, pairs, and successors.
     */
    public String toString() {
        return "ParticipantDataFilePath: " + participantDataFilePath + "\n" + "EventLocationDataFilePath: " +
                eventLocationDataFilePath + "\n" +"EventLocation: " + eventLocation.toString() + "\n" +
                "Unpaired Participants:\n" + getParticipants() + "Pairs: \n" + getPairs() +
                "Participant Successors: \n" + getParticipantSuccessors() +
                "Pair Successors: \n" + getPairSuccessors();
    }

    /**
     * helper method for toString to return one big string of all the unpaired participants
     * @return one string containing all unpaired participants
     */
    public String getParticipants() {
        StringBuilder participantString = new StringBuilder();
        for (Participant participant : participantInputData) {
            participantString.append(participant.toString()).append("\n");
        }
        return participantString.toString();
    }

    /**
     * helper method for toString to return one big string of all the pairs
     * @return one string containing all pairs
     */
    public String getPairs() {
        StringBuilder pairString = new StringBuilder();
        for (Pair pair : pairInputData) {
            pairString.append(pair.toString()).append("\n");
        }
        return pairString.toString();
    }

    /**
     * helper method for toString to return one big string of all the participant successors
     * @return one string containing all participant successors
     */
    public String getParticipantSuccessors() {
        StringBuilder successorString = new StringBuilder();
        for (Participant participant : participantSuccessorList) {
            successorString.append(participant.toString()).append("\n");
        }
        return successorString.toString();
    }

    /**
     * helper method for toString to return one big string of all the pair successors
     * @return one string containing all pair successors
     */
    public String getPairSuccessors() {
        StringBuilder successorString = new StringBuilder();
        for (Pair pair : pairSuccessorList) {
            successorString.append(pair.toString()).append("\n");
        }
        return successorString.toString();
    }

    /**
     * method to get the kitchen count map
     * @return the kitchen count map
     */
    public Map<Kitchen, Integer> getKitchenCountMap() {
        return kitchenCountMap;
    }

}
