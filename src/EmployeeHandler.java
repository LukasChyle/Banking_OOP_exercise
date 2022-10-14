import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeHandler {

    private List<Employee> employees;
    private final Random rnd;
    private final MainDialog dialog;
    private final SimpleDateFormat sdf;

    public EmployeeHandler(MainDialog dialog, SimpleDateFormat sdf) {
        retrieveList();
        rnd = new Random();
        this.dialog = dialog;
        this.sdf = sdf;
    }

    private void storeList() {
        ObjectFileStore.storeObjectList(employees, "employees");
    }

    @SuppressWarnings("unchecked")
    private void retrieveList() {
        List<Employee> employeeList = (List<Employee>) ObjectFileStore.retrieveObjectList("employees");
        if (employeeList != null) {
            employees = employeeList;
        } else {
            employees = new ArrayList<>();
        }
    }

    public void createEmployee() {
        try {
            String firstName = CreateName.setName("first name");
            String lastName = CreateName.setName("last name");
            String pin = CreatePIN.setPIN(false, dialog.getCustomerHandler(), dialog.getEmployeeHandler());
            double salary = setSalary();
            setEmployee(firstName, lastName, pin, salary);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "employee was not created: " + e);
        }
    }

    private void setEmployee(String firstName, String lastName, String pin, double salary) throws IllegalArgumentException {
        if (getEmployeeWithPIN(pin) != null) {
            throw new IllegalArgumentException("employee already exists");
        }
        java.util.Date date = new java.util.Date();
            employees.add(new Employee(firstName, lastName, pin, sdf.format(date), salary, getNewEmployeeID()));
        storeList();
    }

    public void changeSalary(Employee employee) {
        try {
            employee.setSalary(setSalary());
            storeList();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
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

    public Employee getEmployee() {
        String[] options = {"Search employee", "List of employees", "Cancel"};
        int action = JOptionPane.showOptionDialog(null, "Choose how to get employee", "Employee finder",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        try {
            if (action == 0) {
                return searchEmployee();
            } else if (action == 1) {
                return employeeListDialog();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
        }
        return null;
    }

    private Employee searchEmployee() {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "get employee by ID or personal id number");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            input = Format.formatPIN(input);
            input = Format.formatID(input);
            Employee e = getEmployeeWithID(input);
            if (e == null) {
                e = getEmployeeWithPIN(input);
            }
            if (e != null) {
                return e;
            }
            JOptionPane.showMessageDialog(null, "employee was not found");
        }
    }

    private Employee employeeListDialog() {
        List<Employee> employeeList = employees;
        if (employeeList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no employees");
            return null;
        }
        Employee[] employeeArray = employeeList.toArray(Employee[]::new);
        Employee employee = (Employee) JOptionPane.showInputDialog(null, "Choose employee",
                "Employee list", JOptionPane.QUESTION_MESSAGE, null,
                employeeArray, employeeArray[0]);
        if (employee == null) {
            throw new IllegalArgumentException("canceled");
        }
        return employee;
    }

    public Employee getEmployeeWithPIN(String pin) {
        for (Employee e : employees) {
            if (e.getPIN().equals(pin)) {
                return e;
            }
        }
        return null;
    }

    public Employee getEmployeeWithID(String employeeID) {
        for (Employee e : employees) {
            if (e.getEmployeeID().equals(employeeID)) {
                return e;
            }
        }
        return null;
    }

    private String getNewEmployeeID() {
        while (true) {
            char c1 = (char) ('a' + rnd.nextInt(26));
            char c2 = (char) ('a' + rnd.nextInt(26));
            String id = (rnd.nextInt(9000) + 1000) + "-" + c1 + c2;
            if (getEmployeeWithID(id) == null) {
                return id.toUpperCase();
            }
        }
    }

    public void printAllEmployees() {
        StringBuilder allEmployees = new StringBuilder();
        for (Employee e : employees) {
            allEmployees.append(e).append("\n");
        }
        if (allEmployees.toString().isBlank()) {
            JOptionPane.showMessageDialog(null, "there is no employees");
            return;
        }
        ScrollPaneMessage.printMessage(allEmployees.toString(), "All employees", 500);
    }
}


