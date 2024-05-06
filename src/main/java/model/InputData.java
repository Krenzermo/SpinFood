package model;

import model.kitchen.KitchenAvailability;
import model.person.FoodType;
import model.person.Gender;
import model.person.Name;
import model.person.Participant;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;

/**
 * class to save an event location from an external file and save participants and pairs from another external file
 * in the corresponding classes in ArrayLists to return them all together
 */
public class InputData {
    private final ArrayList<Participant> participantInputData;
    private final ArrayList<Pair> pairInputData;
    private Location eventLocation;
    // TODO: add more robust logic for receiving the data
    private static final String participantDataFilePath = "src/main/java/data/teilnehmerliste.csv";
    private static final String eventLocationDataFilePath = "src/main/java/data/partylocation.csv";

    private static InputData inputData; // Singleton

    /**
     * constructor for InputData
     */
    private InputData() {
        this.participantInputData = new ArrayList<>(); // initialize ArrayList for unpaired participants
        this.pairInputData = new ArrayList<>(); // initialize ArrayList for pairs
        saveLocation();
        saveParticipants();
    }

    /**
     * @return InputData instance
     */
    public static InputData getInstance() {
        if (inputData == null) {
            inputData = new InputData();
        }

        return inputData;
    }

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
            eventLocation = new Location(latitude, longitude);        //initialize eventLocation

        } catch (Exception e) {
            System.out.println("Fehler bei Veranstaltungsort: " + e.getMessage());
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
                if (data.hasKitchen == KitchenAvailability.NO) {  //Initialize participant without kitchen

                    Participant participant = new Participant(data.id, data.name, data.foodType, data.age, data.gender);
                    participantInputData.add(participant);

                } else if (parts.length < 11) {

                    Participant participant = new Participant(data.id, data.name, data.foodType, data.age, data.gender, data.hasKitchen, data.kitchenStory, data.kitchenLongitude, data.kitchenLatitude);
                    participantInputData.add(participant);

                } else {

                    Pair pair = getPair(data);
                    pairInputData.add(pair);

                }
            }
        } catch (Exception e) {
            System.out.println("Fehler bei Teilnehmerdaten: " + e.getMessage());
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
        return new Pair(participant1, participant2, true);
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
        int kitchenStory = parts[7].isEmpty() ? 0 : (int) Double.parseDouble(parts[7]);
        double kitchenLongitude = parts[8].isEmpty() ? 0 : Double.parseDouble(parts[8]);
        double kitchenLatitude = parts[9].isEmpty() ? 0 : Double.parseDouble(parts[9]);

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
     * event location data file path, event location, unpaired participants, and pairs.
     */
    public String toString() {
        return "ParticipantDataFilePath: " + participantDataFilePath + "\n" + "EventLocationDataFilePath: " +
                eventLocationDataFilePath + "\n" +"EventLocation: " + eventLocation.toString() + "\n" +
                "unpaired Participants:\n" + getParticipants() + "Pairs: \n" + getPairs();
    }

    /**
     * helper method for toString to return one big string of all the unpaired participants
     * @return one string containing all unpaired participants
     */
    public String getParticipants() {
        String participantString = "";
        for (Participant participantInputDatum : participantInputData) {

            participantString = participantString + participantInputDatum.toString() +"\n";
        }
        return participantString;
    }

    /**
     * helper method for toString to return one big string of all the pairs
     * @return one string containing all pairs
     */
    public String getPairs() {
        String pairString = "";
        for (Pair pairInputDatum : pairInputData) {

            pairString = pairString + pairInputDatum.toString() +"\n";
        }
        return pairString;
    }
}
