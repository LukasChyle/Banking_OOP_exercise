import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LoanHandler {

    private List<Loan> loans;
    private List<Transaction> loanTransactions;
    private List<InterestRateChange> interestChanges;
    private final MainDialog mainDialog;
    private final SimpleDateFormat sdf;

    public LoanHandler(MainDialog mainDialog, SimpleDateFormat sdf) {
        retrieveList();
        retrieveTransactionList();
        retrieveInterestChangesList();
        this.mainDialog = mainDialog;
        this.sdf = sdf;
    }

    private void storeList() {
        ObjectFileStore.storeObjectList(loans, "loans");
    }

    @SuppressWarnings("unchecked")
    private void retrieveList() {
        List<Loan> loanList = (List<Loan>) ObjectFileStore.retrieveObjectList("loans");
        if (loanList != null) {
            loans = loanList;
        } else {
            loans = new ArrayList<>();
        }
    }

    private void storeTransactionList() {
        ObjectFileStore.storeObjectList(loanTransactions, "loanTransactions");
    }

    @SuppressWarnings("unchecked")
    private void retrieveTransactionList() {
        List<Transaction> transactionList = (List<Transaction>) ObjectFileStore.retrieveObjectList("loanTransactions");
        if (transactionList != null) {
            loanTransactions = transactionList;
        } else {
            loanTransactions = new ArrayList<>();
        }
    }

    private void storeInterestChangesList() {
        ObjectFileStore.storeObjectList(interestChanges, "interestChanges");
    }

    @SuppressWarnings("unchecked")
    private void retrieveInterestChangesList() {
        List<InterestRateChange> interestChangesList = (List<InterestRateChange>) ObjectFileStore.retrieveObjectList("interestChanges");
        if (interestChangesList != null) {
            interestChanges = interestChangesList;
        } else {
            interestChanges = new ArrayList<>();
        }
    }

    public void getCustomerLoans(Customer customer) {
        List<Loan> loanList;
        try {
            loanList = getAllLoansWithPIN(customer.getPIN());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
            return;
        }

        Loan[] loanArray = loanList.toArray(Loan[]::new);

        Loan loan = (Loan) JOptionPane.showInputDialog(null, "Choose loan",
                "Loans of " + customer.getPIN(), JOptionPane.QUESTION_MESSAGE, null,
                loanArray, loanArray[0]);
        if (loan == null) {
            return;
        }
        mainDialog.loanDialog(loan);
    }

    public void createLoan(Customer customer) {
        JOptionPane.showMessageDialog(null, "employee have to grant the loan");
        try {
            Employee employee = mainDialog.getEmployeeHandler().getEmployee();
            if (employee == null) {
                return;
            }
            java.util.Date date = new java.util.Date();
            Loan loan = setLoan(customer, setLoanAmount(), setLoanInterest(), employee.getEmployeeID());
            createInterestChange(loan, loan.getInterest(), sdf.format(date), loan.getGrantedBy());
            storeInterestChangesList();
            JOptionPane.showMessageDialog(null, "new loan created with ID: " + loan.getLoanID());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "loan not created: " + e);
        }
    }

    private Loan setLoan(Customer customer, double amount, double interest, String grantedBy) throws IllegalArgumentException {
        String accountID = CreateAccountID.setID(mainDialog.getAccountHandler(), mainDialog.getLoanHandler());
        java.util.Date date = new java.util.Date();
        loans.add(new Loan(accountID, customer.getPIN(), amount, interest, grantedBy, sdf.format(date)));
        storeList();
        return getLoanWithID(accountID);
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

    public void makePayment(Loan loan) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter sum you are paying");
            if (input == null) {
                return;
            }
            java.util.Date date = new java.util.Date(); //updates the timeStamp
            try {
                loan.makePayment(Double.parseDouble(input));
                createTransaction(Double.parseDouble(input), loan, sdf.format(date));
                storeTransactionList();
                storeList();
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "input not valid");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
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

    public void setNewInterest(Loan loan) {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Current interest " + (loan.getInterest() * 100) + "%, set new interest in %");
            if (input == null) {
                return;
            }
            java.util.Date date = new java.util.Date();
            JOptionPane.showMessageDialog(null, "employee have to grant the change");
            try {
                Employee employee = mainDialog.getEmployeeHandler().getEmployee();
                if (employee == null) {
                    return;
                }
                loan.makeInterestChange(Double.parseDouble(input) / 100);
                createInterestChange(loan, (Double.parseDouble(input)/ 100), sdf.format(date), loan.getGrantedBy());
                storeInterestChangesList();
                storeList();
                JOptionPane.showMessageDialog(null, "interest set to " + input + "%");
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "interest input not valid");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }
    }

    public void printPayments(Loan loan) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction t : loanTransactions) {
            if (t.id().equals(loan.getLoanID())) {
                transactions.add(t);
            }
        }
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no payments");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        ScrollPaneMessage.printMessage(sb.toString(), "All payments", 400);
    }

    public Loan getLoanWithID(String loanID) {
        for (Loan l : loans) {
            if (l.getLoanID().equals(loanID)) {
                return l;
            }
        }
        return null;
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

    public double getLoansSum(String pin) {
        double loanSum = 0;
        for (Loan l : loans) {
            if (l.getOwnerPIN().equals(pin)) {
                loanSum += l.getAmount();
            }
        }
        return loanSum;
    }

    public void printLoansGranted(Employee employee) {
        StringBuilder sb = new StringBuilder();
        for (Loan l : loans) {
            if (l.getGrantedBy().equals(employee.getEmployeeID())) {
                sb.append(l).append("\n");
            }
        }
        if (sb.isEmpty()) {
            JOptionPane.showMessageDialog(null, employee.getEmployeeID() + " has not granted any loans");
        } else {
            ScrollPaneMessage.printMessage(sb.toString(), "All loans granted by " + employee.getEmployeeID(), 750);
        }
    }

    public void printInterestChanges(Loan loan) {
        StringBuilder sb = new StringBuilder();
        for (InterestRateChange i : interestChanges) {
            if (i.id().equals(loan.getLoanID())) {
                sb.append(i).append("\n");
            }
        }
        if (sb.isEmpty()) {
            JOptionPane.showMessageDialog(null, "no changes have been made");
        } else {
            ScrollPaneMessage.printMessage(sb.toString(), "All interest changes", 500);
        }
    }

    public void printInterestChanges(Employee employee) {
        StringBuilder sb = new StringBuilder();
        for (InterestRateChange i : interestChanges) {
            if (i.grantedBy().equals(employee.getEmployeeID())) {
                sb.append(i).append("\n");
            }
        }
        if (sb.isEmpty()) {
            JOptionPane.showMessageDialog(null, employee.getEmployeeID() + " has not changed the interest of any loans");
        } else {
            ScrollPaneMessage.printMessage(sb.toString(), "All interest of loans changed by " + employee.getEmployeeID(), 500);
        }
    }

    private void createTransaction(double amount, Loan loan, String timestamp) {
        loanTransactions.add(new Transaction(amount, loan.getLoanID(), timestamp));
    }

    public void createInterestChange(Loan loan, double interest, String timestamp, String grantedBy) {
        interestChanges.add(new InterestRateChange(loan.getLoanID(), interest, timestamp, grantedBy));
    }
}
