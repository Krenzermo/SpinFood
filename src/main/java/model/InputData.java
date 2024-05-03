package model;

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
    private ArrayList<Participant> participantInputData; // list of only the unpaired participants
    private ArrayList<Pair> pairInputData; // list of all pairs
    private static Location eventLocation; // location of the event
    private String participantDataFilePath; // file path for participant file
    private String eventLocationDataFilePath; // file path for event location file

    /**
     * constructor for InputData
     * @param participantDataFilePath file path for the participants file
     * @param eventLocationDataFilePath file path for event location file
     */
    public InputData(String participantDataFilePath, String eventLocationDataFilePath) {
        this.participantDataFilePath = participantDataFilePath;
        this.eventLocationDataFilePath = eventLocationDataFilePath;
        this.participantInputData = new ArrayList<>(); // initialize ArrayList for unpaired participants
        this.pairInputData = new ArrayList<>(); // initialize ArrayList for pairs
        saveLocation();
        saveParticipants();
    }

    /**
     * method to save the EventLocation
     */
    public void saveLocation() {
        try (Scanner scanner = new Scanner(Paths.get(this.eventLocationDataFilePath))) {     //build scanner with eventLocation file
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
        try (Scanner scanner = new Scanner(Paths.get(this.participantDataFilePath))) { // build scanner with participant file
            scanner.nextLine();                 // skip headline

            while (scanner.hasNextLine()) {           //iterate over all lines one by one
                String line = scanner.nextLine();         //initialize read line as variable each loop
                String[] parts = line.split(",(?=\\S)|(?<=\\S),"); // split current line by denominator ","
                                                                         // excluding " without any more data following

                // extract data from the parts Array into temporary variables to build the participant objects
                String id = parts[1];
                Name name = new Name(parts[2], "");       //leaving last name empty
                FoodType foodType = FoodType.valueOf(parts[3].toUpperCase());
                byte age = Byte.parseByte(parts[4]);
                Gender gender = Gender.valueOf(parts[5].toUpperCase());
                boolean hasKitchen = parts[6].equals("yes");      //todo ändern mit Boolean class oder enum

                if (!(hasKitchen)) {           //if kitchen is missing, initialize values with 0
                    int kitchenStory = 0;
                    double kitchenLongitude = 0;
                    double kitchenLatitude = 0;       //todo mit neuem konstruktor aktualisieren

                    Participant participant = new Participant(id, name, foodType, age, gender, false, kitchenStory, kitchenLongitude, kitchenLatitude);
                    participantInputData.add(participant);    //build participant and add to ArrayList
                    continue;                                   // restart while loop with next line
                }

                //if empty, fill up empty kitchenStory with value 0, as the input field for story 0 kitchens is empty
                int kitchenStory = parts[7].isEmpty() ? 0 : (int) Double.parseDouble(parts[7]);
                double kitchenLongitude = Double.parseDouble(parts[8]);
                double kitchenLatitude = Double.parseDouble(parts[9]);

                if (parts.length < 11) {           // participant if less than 11 input fields are filled
                    Participant participant = new Participant(id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    participantInputData.add(participant);         //build and add participant
                } else {                            //else it´s a pair
                    String idTwo = parts[10];
                    Name nameTwo = new Name(parts[11], "");
                    byte ageTwo = (byte) Double.parseDouble(parts[12]);      // age of partner is in the input data is stored as double, so we make it a byte again
                    Gender genderTwo = Gender.valueOf(parts[13].toUpperCase());

                    //foodType and kitchen values are shared between participants in a pair
                    Participant participant1 = new Participant(id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    Participant participant2 = new Participant(idTwo, nameTwo, foodType, ageTwo, genderTwo, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    Pair pair = new Pair(participant1, participant2, true);    //build two participants and make them a pair
                    pairInputData.add(pair);                                                   // add the pair to the ArrayList for pairs
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler bei Teilnehmerdaten: " + e.getMessage());
        }
    }

    /**
     * method to get the event location
     * @return the Location eventLocation
     */
    public static Location getEventLocation() {
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