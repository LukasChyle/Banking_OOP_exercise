public class Customer extends Person {

    public Customer(String firstName, String lastName, String pin, String timestamp) {
        super(firstName, lastName, pin, timestamp);
    }

    @Override
    public String toString() {
        return "  " + getFirstName() + " " + getLastName() +
                ", PIN: " + getPIN() + " , created: " + getDateCreated();
    }
}
