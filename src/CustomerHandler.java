import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerHandler {

    private final List<Customer> customers;
    private final MainDialog dialog;
    private final SimpleDateFormat sdf;


    public CustomerHandler(MainDialog dialog, SimpleDateFormat sdf) {
        customers = new ArrayList<>();
        this.dialog = dialog;
        this.sdf = sdf;
    }

    public void createCustomer() {
        try {
            String firstName = CreateName.setName("first name");
            String lastName = CreateName.setName("last name");
            String pin = CreatePIN.setPIN(false, dialog.getCustomerHandler(), dialog.getEmployeeHandler());
            setCustomer(firstName, lastName, pin);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "customer was not created: " + e);
        }
    }

    private void setCustomer(String firstName, String lastName, String pin) throws IllegalArgumentException {
        if (getCustomerWithPIN(pin) != null) {
            throw new IllegalArgumentException("customer already exists");
        }
        java.util.Date date = new java.util.Date(); //updates the timeStamp
        customers.add(new Customer(firstName, lastName, pin, sdf.format(date)));
    }

    public Customer getCustomer() {
        String[] options = {"Search customer", "List of customer", "Cancel"};
        int action = JOptionPane.showOptionDialog(null, "Choose how to get customer", "Customer finder",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        try {
            if (action == 0) {
                return searchCustomer();
            } else if (action == 1) {
                return customerListDialog();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
        }
        return null;
    }

    private Customer searchCustomer() {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "get customer by ID or personal id number");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            input = Format.formatPIN(input);
            Customer c = getCustomerWithPIN(input);
            if (c != null) {
                return c;
            }
            JOptionPane.showMessageDialog(null, "customer was not found");
        }
    }

    private Customer customerListDialog() {
        List<Customer> customerList = customers;
        if (customerList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no customers");
            return null;
        }
        Customer[] customerArray = customerList.toArray(Customer[]::new);
        Customer customer = (Customer) JOptionPane.showInputDialog(null, "Choose customer",
                "Customer list", JOptionPane.QUESTION_MESSAGE, null,
                customerArray, customerArray[0]);
        if (customer == null) {
            throw new IllegalArgumentException("canceled");
        }
        return customer;
    }

    public Customer getCustomerWithPIN(String pin) {
        for (Customer c : customers) {
            if (c.getPIN().equals(pin)) {
                return c;
            }
        }
        return null;
    }

    public void printAllCustomers() {
        StringBuilder allCustomers = new StringBuilder();
        for (Customer c : customers) {
            allCustomers.append(c).append("\n");
        }
        if (allCustomers.toString().isBlank()) {
            JOptionPane.showMessageDialog(null, "there is no customers");
            return;
        }
        ScrollPaneMessage.printMessage(allCustomers.toString(), "All customers", 500);
    }
}