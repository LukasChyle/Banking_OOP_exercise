import javax.swing.*;
import java.text.SimpleDateFormat;

public class MainDialog {

    private final CustomerHandler customerHandler;
    private final EmployeeHandler employeeHandler;
    private final AccountHandler accountHandler;
    private final LoanHandler loanHandler;

    public MainDialog() {
        CreatePIN createPIN = new CreatePIN();
        CreateAccountID createID = new CreateAccountID();
        ScrollPaneMessage spm = new ScrollPaneMessage();
        Format format = new Format();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd '-' HH:mm");
        CreateName setName = new CreateName();
        customerHandler = new CustomerHandler(this, createPIN, spm, format, sdf, setName);
        employeeHandler = new EmployeeHandler(this, createPIN, spm, format, sdf, setName);
        accountHandler = new AccountHandler(this, createID, spm, sdf);
        loanHandler = new LoanHandler(this, createID, spm, sdf);


        String[] options = {"Add Employee", "Add Customer", "Manage Employee", "Manage Customer",
                "List Employees", "List Customers", "Accounts: change interest"};
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, "Choose your next action", "Main Menu",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1 -> run = false;
                case 0 -> employeeHandler.createEmployee();
                case 1 -> customerHandler.createCustomer();
                case 2 -> getEmployee();
                case 3 -> getCustomer();
                case 4 -> employeeHandler.printAllEmployees();
                case 5 -> customerHandler.printAllCustomers();
                case 6 -> accountHandler.setAccountInterest();
            }
        }
        JOptionPane.showMessageDialog(null, "exit program");
    }

    private void getEmployee() {
        try {
            Employee e = employeeHandler.getEmployee();
            if (e != null) {
                employeeDialog(e);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
        }

    }

    private void getCustomer() {
        try {
            Customer c = customerHandler.getCustomer();
            if (c != null) {
                customerDialog(c);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
        }
    }

    private void customerDialog(Customer customer) {
        String[] options = {"Create account", "Create loan", "Manage account", "Manage loan", "Return"};
        boolean run = true;

        while (run) {
            String message = customer + "\nAccounts: " + accountHandler.getNumberOfAccountsPIN(customer.getPIN()) +
                    " , Sum of balance: " + accountHandler.getAccountsSum(customer.getPIN()) +
                    "\nLoans: " + loanHandler.getNumberOfLoansPIN(customer.getPIN()) +
                    " , Sum of loan: " + loanHandler.getLoansSum(customer.getPIN());

            int action = JOptionPane.showOptionDialog(null, message, "Customer",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 4 -> run = false;
                case 0 -> accountHandler.createAccount(customer);
                case 1 -> loanHandler.createLoan(customer);
                case 2 -> accountHandler.getCustomerAccounts(customer);
                case 3 -> loanHandler.getCustomerLoans(customer);
            }
        }
    }


    private void employeeDialog(Employee employee){ // not done

        String[] options = {"Change salary", "Loans granted", };
        String message = employee + "\nSalary: " + employee.getSalary();
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, message, "Employee",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 6 -> run = false;

            }
        }
    }

    public void accountDialog(Account account) {
        String[] options = {"Deposit", "Withdraw", "Transactions", "Return"};
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, account, "Account",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 3 -> run = false;
                case 0 -> accountHandler.makeTransaction(account, true);
                case 1 -> accountHandler.makeTransaction(account, false);
                case 2 -> accountHandler.printTransactions(account);
            }
        }
    }

    public void loanDialog(Loan loan) {
        String[] options = {"Make payment", "Change Interest", "Payment history", "Interest history", "Return"};
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, loan, "Loan",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 4 -> run = false;
                case 0 -> loanHandler.makePayment(loan);
                case 1 -> loanHandler.setNewInterest(loan);
                case 2 -> loanHandler.printPayments(loan);
                case 3 -> loanHandler.printInterestChanges(loan);
            }
        }
    }

    public CustomerHandler getCustomerHandler() {
        return customerHandler;
    }

    public EmployeeHandler getEmployeeHandler() {
        return employeeHandler;
    }

    public AccountHandler getAccountHandler() {
        return accountHandler;
    }

    public LoanHandler getLoanHandler() {
        return loanHandler;
    }
}