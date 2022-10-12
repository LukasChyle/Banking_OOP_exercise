import javax.swing.*;

public class MainDialog {

    private final Data data;
    private final CustomerHandler customerHandler;
    private final EmployeeHandler employeeHandler;

    public MainDialog() {
        data = new Data();
        customerHandler = new CustomerHandler(data);
        employeeHandler = new EmployeeHandler(data);

        String[] options = {"Add\nEmployee", "Add\nCustomer", "Manage\nEmployee", "Manage\nCustomer",
                "List\nEmployees", "List\nCustomers", "Accounts:\nSet interest value"};
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, "Choose your next action", "Main Menu",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1 -> run = false;
                case 0 -> createEmployee();
                case 1 -> createCustomer();
                case 2 -> getEmployee();
                case 3 -> getCustomer();
                case 4 -> data.printAllEmployees();
                case 5 -> data.printAllCustomers();
                case 6 -> setAccountInterest();
            }
        }
        JOptionPane.showMessageDialog(null, "exit program");
    }

    private void createEmployee() {
        try {
            String firstName = setName("first name");
            String lastName = setName("last name");
            String pin = setPIN(false);
            double salary = setSalary();
            data.createEmployee(firstName, lastName, pin, salary);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "employee was not created: " + e);
        }
    }

    private void createCustomer() {
        try {
            String firstName = setName("first name");
            String lastName = setName("last name");
            String pin = setPIN(true);
            data.createCustomer(firstName, lastName, pin);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "customer was not created: " + e);
        }
    }

    private String setName(String type) {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "enter " + type);
            if (name == null) {
                throw new IllegalArgumentException("canceled");
            }
            name = name.trim();
            if (!name.chars().allMatch(Character::isLetter) || name.isBlank()) {
                JOptionPane.showMessageDialog(null, type + " is not valid");
                continue;
            }
            return name;
        }
    }

    private String setPIN(boolean customer) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter 10 digit personal identity number");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            input = input.trim();
            if (input.length() == 10 && input.chars().allMatch(Character::isDigit)) {
                input = input.substring(0, 6) + "-" + input.substring(6);
            }
            if (input.length() == 11 && input.substring(0, 6).chars().allMatch(Character::isDigit) &&
                    input.charAt(6) == '-' && input.substring(7).chars().allMatch(Character::isDigit)) {
                if (customer && data.getCustomerWithPIN(input) == null) {
                    return input;
                } else if (!customer && data.getEmployeeWithPIN(input) == null) {
                    return input;
                }
                JOptionPane.showMessageDialog(null, "this personal identity number is already in use");
                continue;
            }
            JOptionPane.showMessageDialog(null, "personal identity number is not valid");
        }
    }

    private double setSalary() {
        while (true) {
            String salary = JOptionPane.showInputDialog(null, "enter monthly salary");
            if (salary == null) {
                throw new IllegalArgumentException("canceled");
            }
            salary = salary.trim();
            if (salary.chars().allMatch(Character::isDigit) && !salary.isBlank() && Double.parseDouble(salary) != 0) {
                return Double.parseDouble(salary);
            }
            JOptionPane.showMessageDialog(null, "salary is not valid");
        }
    }

    private void getEmployee() {
        String input = JOptionPane.showInputDialog(null, "get employee by ID or personal id number");
        if (input == null) {
            return;
        }
        input = data.formatPIN(input);
        input = data.formatID(input);
        Employee e = data.getEmployeeWithID(input);
        if (e == null) {
            e = data.getEmployeeWithPIN(input);
        }
        if (e != null) {
            employeeHandler.dialog(e);
            return;
        }
        JOptionPane.showMessageDialog(null, "employee was not found");
    }

    private void getCustomer() {
        String input = JOptionPane.showInputDialog(null, "get customer by personal id number");
        if (input == null) {
            return;
        }
        input = data.formatPIN(input);
        Customer c = data.getCustomerWithPIN(input);
        if (c != null) {
            customerHandler.dialog(c);
            return;
        }
        JOptionPane.showMessageDialog(null, "customer was not found");
    }

    private void setAccountInterest() {
        while(true) {
            String input = JOptionPane.showInputDialog(null, "enter interest for all accounts in %");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            try {
                Account.setInterest(Double.parseDouble(input) / 100);
                JOptionPane.showMessageDialog(null, "interest set to " + input + "%");
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "interest input not valid");
            }
        }
    }
}