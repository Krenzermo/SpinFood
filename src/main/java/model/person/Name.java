package model.person;

public record Name(String firstName, String lastName) {
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}