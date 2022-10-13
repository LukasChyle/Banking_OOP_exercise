import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Data {

    private final List<Customer> customers;
    private final List<Employee> employees;
    private final List<Account> accounts;
    private final List<Loan> loans;
    private final Random rnd;
    private final SimpleDateFormat sdf;

    public Data() {
        sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        customers = new ArrayList<>();
        employees = new ArrayList<>();
        accounts = new ArrayList<>();
        loans = new ArrayList<>();
        rnd = new Random();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void createCustomer(String firstName, String lastName, String pin) throws IllegalArgumentException {
        if (getCustomerWithPIN(pin) != null) {
            throw new IllegalArgumentException("customer already exists");
        }
        customers.add(new Customer(firstName, lastName, pin));
    }

    public void createEmployee(String firstName, String lastName, String pin, double salary) throws IllegalArgumentException {
        if (getEmployeeWithPIN(pin) != null) {
            throw new IllegalArgumentException("employee already exists");
        }
        employees.add(new Employee(firstName, lastName, pin, salary, getNewEmployeeID()));
    }

    public String createAccount(Customer customer) throws IllegalArgumentException {
        String accountID = getNewAccountID();
        java.util.Date date = new java.util.Date(); //updates the timeStamp
        accounts.add(new Account(accountID, customer.getPIN(), sdf.format(date)));
        return accountID;
    }

    public String createLoan(Customer customer, double amount, double interest, Employee employee) throws IllegalArgumentException {
        String accountID = getNewAccountID();
        java.util.Date date = new java.util.Date(); //updates the timeStamp
        loans.add(new Loan(accountID, customer.getPIN(), amount, interest, employee.getEmployeeID(), sdf.format(date)));
        return accountID;
    }

    public Customer getCustomerWithPIN(String pin) {
        for (Customer c : customers) {
            if (c.getPIN().equals(pin)) {
                return c;
            }
        }
        return null;
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

    private String getNewAccountID() {
        while (true) {
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                id.append(rnd.nextInt(10));
            }
            if (getAccountWithID(id.toString()) == null && getLoanWithID(id.toString()) == null) {
                return id.toString();
            }
        }
    }

    public Account getAccountWithID(String accountID) {
        for (Account a : accounts) {
            if (a.getAccountID().equals(accountID)) {
                return a;
            }
        }
        return null;
    }

    public Loan getLoanWithID(String loanID) {
        for (Loan l : loans) {
            if (l.getLoanID().equals(loanID)) {
                return l;
            }
        }
        return null;
    }

    public int getNumberOfAccountsPIN(String pin) {
        int number = 0;
        for (Account a : accounts) {
            if (a.getOwnerPIN().equals(pin)) {
                number++;
            }
        }
        return number;
    }

    public int getNumberOfLoansPIN(String pin) {
        int number = 0;
        for (Loan l : loans) {
            if (l.getOwnerPIN().equals(pin)) {
                number++;
            }
        }
        return number;
    }

    public List<Account> getAllAccountsWithPIN(String pin) {
        List<Account> accountList = new ArrayList<>();
        for (Account a : accounts) {
            if (a.getOwnerPIN().equals(pin)) {
                accountList.add(a);
            }
        }
        if (accountList.isEmpty()) {
            throw new IllegalArgumentException("customer has no accounts");
        }
        return accountList;
    }

    public List<Loan> getAllLoansWithPIN(String pin) {
        List<Loan> loanList = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getOwnerPIN().equals(pin)) {
                loanList.add(l);
            }
        }
        if (loanList.isEmpty()) {
            throw new IllegalArgumentException("customer has no loans");
        }
        return loanList;
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
        JOptionPane.showMessageDialog(null, allCustomers.toString());
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
        JOptionPane.showMessageDialog(null, allEmployees.toString());
    }

    public double getAccountsSum(String pin){
        double balanceSum = 0;
        for (Account a : accounts) {
            if (a.getOwnerPIN().equals(pin)) {
                balanceSum += a.getBalance();
            }
        }
        return balanceSum;
    }

    public double getLoansSum(String pin){
        double loanSum = 0;
        for (Loan l : loans) {
            if (l.getOwnerPIN().equals(pin)) {
                loanSum += l.getAmount();
            }
        }
        return loanSum;
    }

    public String formatPIN(String input){
        input = input.trim();
        if (input.length() == 10) {
            input = input.substring(0, 6) + "-" + input.substring(6);
        }
        return input;
    }

    public String formatID(String input) {
        input = input.trim();
        if (input.length() == 6) {
            input = input.substring(0, 4) + "-" + input.substring(4);
        }
        return input;
    }
}


