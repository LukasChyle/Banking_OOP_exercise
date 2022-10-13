import javax.swing.*;
import java.util.List;

public class CustomerHandler {

    private final AccountHandler accountHandler;
    private final LoanHandler loanHandler;

    private final Data data;

    public CustomerHandler(Data data) {
        this.data = data;
        accountHandler = new AccountHandler();
        loanHandler = new LoanHandler(data);
    }

    public void dialog(Customer customer) {

        String[] options = {"Create account", "Create loan", "Manage account", "Manage loan", "Return"};
        boolean run = true;

        while (run) {
            String message = customer + "\nAccounts: " + data.getNumberOfAccountsPIN(customer.getPIN()) +
                    " , Sum of balance: " + data.getAccountsSum(customer.getPIN()) + "\nLoans: " + data.getNumberOfLoansPIN(customer.getPIN()) +
                    " , Sum of loan: " + data.getLoansSum(customer.getPIN());


            int action = JOptionPane.showOptionDialog(null, message, "Customer",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 6 -> run = false;
                case 0 -> createAccount(customer);
                case 1 -> createLoan(customer);
                case 2 -> getCustomerAccounts(customer);
                case 3 -> getLoan(customer);
            }
        }
    }

    private void createAccount(Customer customer) {
        try {
            String accountID = data.createAccount(customer);
            JOptionPane.showMessageDialog(null, "new account created with ID: " + accountID);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "account not created: " + e);
        }
    }

    private void createLoan(Customer customer) {
        try {
            String loanID = data.createLoan(customer, setLoanAmount(), setLoanInterest(), setGrantedBy());
            JOptionPane.showMessageDialog(null, "new loan created with ID: " + loanID);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "loan not created: " + e);
        }
    }

    private double setLoanAmount() {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter sum of the loan");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            } else if (input.chars().allMatch(Character::isDigit) && Double.parseDouble(input) != 0) {
                return Double.parseDouble(input);
            }
            JOptionPane.showMessageDialog(null, "loan sum not valid");
        }
    }

    private double setLoanInterest() {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter interest of loan in %");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            try {
                return Double.parseDouble(input) / 100;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "loan interest not valid");
            }
        }
    }

    private Employee setGrantedBy() {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "employee-ID or personal id number of employee that grants the loan");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            input = data.formatPIN(input);
            input = data.formatID(input);
            Employee e = data.getEmployeeWithID(input);
            if (e == null) {
                e = data.getEmployeeWithPIN(input);
            }
            if (e != null) {
                return e;
            }
            JOptionPane.showMessageDialog(null, "employee was not found");
        }
    }

    private void getCustomerAccounts(Customer customer) {
        List<Account> accountList = data.getAllAccountsWithPIN(customer.getPIN());
        if (accountList.isEmpty()) {
            JOptionPane.showMessageDialog(null, customer.getPIN() + " don't have any accounts");
        }

        Account[] accountArray = accountList.toArray(Account[]::new);

        Account action = (Account) JOptionPane.showInputDialog(null, "Choose account",
                "Accounts of " + customer.getPIN(), JOptionPane.QUESTION_MESSAGE, null,
                accountArray, accountArray[0]);
        if (action == null) {
            return;
        }
        accountHandler.dialog(action);
    }

    private void getLoan(Customer customer) {
        String input = JOptionPane.showInputDialog(null, "get loan by ID");
        if (input == null) {
            return;
        }
        input = input.trim();
        Loan l = data.getLoanWithID(input);
        if (l != null) {
            if (l.getOwnerPIN().equals(customer.getPIN())) {
                loanHandler.dialog(l);
                return;
            }
            JOptionPane.showMessageDialog(null, "this loan don't belong to this customer");
            return;
        }
        JOptionPane.showMessageDialog(null, "loan was not found");
    }

    /* Methods not in use

        private void getAccount(Customer customer) {
        String input = JOptionPane.showInputDialog(null, "get account by ID");
        if (input == null) {
            return;
        }
        input = input.trim();
        Account a = data.getAccountWithID(input);
        if (a != null) {
            if (a.getOwnerPIN().equals(customer.getPIN())) {
                accountHandler.dialog(a);
                return;
            }
            JOptionPane.showMessageDialog(null, "this account don't belong to this customer");
            return;
        }
        JOptionPane.showMessageDialog(null, "account was not found");
    }

    private void printAccounts(Customer customer) {
        List<Account> accounts;
        try {
            accounts = data.getAllAccountsWithPIN(customer.getPIN());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return;
        }
        StringBuilder sb = new StringBuilder().append("Accounts:\n");
        for (Account a : accounts) {
            sb.append(a).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void printLoans(Customer customer) {
        List<Loan> loans;
        try {
            loans = data.getAllLoansWithPIN(customer.getPIN());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return;
        }
        StringBuilder sb = new StringBuilder().append("Loans:\n");
        for (Loan l : loans) {
            sb.append(l).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

     */
}