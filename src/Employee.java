public class Employee extends Person {

    private double salary;
    private final String employeeID;

    public Employee(String firstName, String lastName, String pin, double salary, String employeeID) {
        super(firstName, lastName, pin);
        if (salary <= 0) {
            throw new IllegalArgumentException("salary can't be negative");
        } else if (employeeID.length() != 7) {
            throw new IllegalArgumentException("employeeID is not valid");
        }
        this.salary = salary;
        this.employeeID = employeeID;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary <= 0) {
            throw new IllegalArgumentException("salary can't be negative");
        }
        this.salary = salary;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    @Override
    public String toString() {
        return "  " + getFirstName() + " " + getLastName() + ", personal id number: " + getPIN() + ", ID: " + employeeID;
    }
}
