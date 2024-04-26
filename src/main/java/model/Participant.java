package model;

public class Participant {

    private int id;
    private String name;
    private FoodPreference foodPreference;
    private int age;
    private Sex sex;
    private String email;

    public Participant(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age + ", Email: " + email;
    }
}
