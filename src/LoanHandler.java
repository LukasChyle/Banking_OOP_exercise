import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LoanHandler {

    private final List<Loan> loans;
    private final MainDialog mainDialog;
    private final CreateAccountID createID;
    private final ScrollPaneMessage spm;
    private final SimpleDateFormat sdf;

    public LoanHandler(MainDialog mainDialog, CreateAccountID createID,
                       ScrollPaneMessage spm, SimpleDateFormat sdf) {
        loans = new ArrayList<>();
        this.mainDialog = mainDialog;
        this.spm = spm;
        this.sdf = sdf;
        this.createID = createID;
    }

    public void getCustomerLoans(Customer customer) {
        List<Loan> loanList = getAllLoansWithPIN(customer.getPIN());
        if (loanList.isEmpty()) {
            JOptionPane.showMessageDialog(null, customer.getPIN() + " don't have any loans");
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
            String loanID = setLoan(customer, setLoanAmount(), setLoanInterest(), employee.getEmployeeID());
            JOptionPane.showMessageDialog(null, "new loan created with ID: " + loanID);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "loan not created: " + e);
        }
    }

    private String setLoan(Customer customer, double amount, double interest, String grantedBy) throws IllegalArgumentException {
        String accountID = createID.setID(mainDialog.getAccountHandler(), mainDialog.getLoanHandler());
        java.util.Date date = new java.util.Date();
        loans.add(new Loan(accountID, customer.getPIN(), amount, interest, grantedBy, sdf.format(date)));
        return accountID;
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
                loan.makePayment(Double.parseDouble(input), sdf.format(date));
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
                Employee employee =  mainDialog.getEmployeeHandler().getEmployee();
                if (employee == null) {
                    return;
                }
                loan.makeInterestChange(Double.parseDouble(input) / 100, sdf.format(date), employee.getEmployeeID());
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
        List<Transaction> transactions = loan.getTransactions();
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no payments");
            return;
        }
        spm.printMessage(sb.toString(), "All payments", 400);
    }

    public void printInterestChanges(Loan loan) {
        List<InterestRateChange> interestChanges = loan.getInterestChanges();
        StringBuilder sb = new StringBuilder();
        for (InterestRateChange i : interestChanges) {
            sb.append(i).append("\n");
        }
        if (interestChanges.isEmpty()) {
            JOptionPane.showMessageDialog(null, "no changes have been made");
            return;
        }
        spm.printMessage(sb.toString(), "All interest changes", 400);
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

}
