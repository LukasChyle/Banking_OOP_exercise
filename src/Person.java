import java.io.Serializable;

public abstract class Person implements Serializable {

    private final String firstName;
    private final String lastName;
    private final String pin; // personal identity number
    private final String dateCreated;


    public Person(String firstName, String lastName, String pin, String timestamp) {
        if (firstName.isBlank() || lastName.isBlank()){
            throw new IllegalArgumentException("first name/surname can't be blank");
        } else if (String.valueOf(pin).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.pin = pin;
        this.dateCreated = timestamp;
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

    public String getDateCreated() {
        return dateCreated;
    }
}
