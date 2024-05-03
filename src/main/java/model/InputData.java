package model;
import model.person.FoodType;
import model.person.Gender;
import model.person.Name;
import model.person.Participant;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;

public class InputData {
    private ArrayList<Participant> participantInputData;
    private ArrayList<Pair> pairInputData;
    private Location eventLocation;
    private final String participantDataFileName;
    private final String eventLocationDataFileName;
    public InputData(String participantDataFileName, String eventLocationDataFileName){
        this.participantDataFileName = participantDataFileName;
        this.eventLocationDataFileName = eventLocationDataFileName;
        this.participantInputData = new ArrayList<>(); // Initialize participantInputData
        this.pairInputData = new ArrayList<>(); // Initialize pairInputData
        saveLocation();
        saveParticipants();
    }
    public void saveLocation(){
        try (Scanner scanner = new Scanner(Paths.get(this.eventLocationDataFileName))) {
            scanner.nextLine(); //skip headline

            String line = scanner.nextLine();
            String[] parts = line.split(",");

            double longitude = Double.parseDouble(parts[0]);
            double latitude = Double.parseDouble(parts[1]);
            this.eventLocation = new Location (latitude, longitude);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void saveParticipants(){

        try (Scanner scanner = new Scanner(Paths.get(this.participantDataFileName))) {
            scanner.nextLine(); //skip headline

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",(?=\\S)|(?<=\\S),"); //split while excluding empty spaces

                String id = parts[1];
                Name name = new Name(parts[2],"");
                FoodType foodType = FoodType.valueOf(parts[3].toUpperCase());
                byte age = Byte.parseByte(parts[4]);
                Gender gender = Gender.valueOf(parts[5].toUpperCase());
                boolean hasKitchen = parts[6].equals("yes"); //todo enum machen?
                if (!(hasKitchen)){
                    int kitchenStory = 0;
                    double kitchenLongitude = 0;
                    double kitchenLatitude = 0;
                    Participant participant = new Participant (id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    participantInputData.add(participant);
                    //todo make participant with no kitchen, neuen konstruktor machen?
                    continue;
                }
                int kitchenStory = Integer.parseInt(parts[7]);
                double kitchenLongitude = Double.parseDouble(parts[8]);
                double kitchenLatitude = Double.parseDouble(parts[9]);


                if (parts.length < 10){
                    Participant participant = new Participant (id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    participantInputData.add(participant);
                    //todo muss man jeden neu hinzugefügten participant neu bennenen?

                } else{
                    String idTwo = parts[10];
                    Name nameTwo = new Name (parts[11],"");
                    FoodType foodTypeTwo = FoodType.valueOf(parts[12].toUpperCase());
                    byte ageTwo = Byte.parseByte(parts[13]);
                    Participant participant1 = new Participant (id, name, foodType, age, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    Participant participant2 = new Participant (idTwo, nameTwo, foodTypeTwo, ageTwo, gender, hasKitchen, kitchenStory, kitchenLongitude, kitchenLatitude);
                    Pair pair = new Pair(participant1, participant2, true);
                    pairInputData.add(pair);
                    //todo same wie oben mit namen
                }

            }

        } catch (Exception e) {
            System.out.println("Error; " + e.getMessage());
        }
    }
    public Location getEventLocation(){
        return eventLocation;
    }

}
