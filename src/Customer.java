public class Customer extends Person {

    public Customer(String firstName, String lastName, String pin) {
        super(firstName, lastName, pin);
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + ", personal id number: " + getPIN();
    }
}
