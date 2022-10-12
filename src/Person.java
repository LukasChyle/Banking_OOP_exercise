public abstract class Person {

    private final String firstName;
    private final String lastName;
    private final String pin; // Social security number

    public Person(String firstName, String lastName, String pin) {
        if (firstName.isBlank() || lastName.isBlank()){
            throw new IllegalArgumentException("first name/surname can't be blank");
        } else if (String.valueOf(pin).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.pin = pin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPIN() {
        return pin;
    }
}
