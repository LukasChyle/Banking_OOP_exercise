import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loan {

    private final String loanID;
    private double amount;
    private double interest;
    private final String grantedBy;
    private final List<InterestRateChange> interestChanges;
    private final List<Transaction> transactions;
    private final String ownerPIN;

    public Loan(String loanID, String ownerPIN, double amount, double interest, String grantedBy) {
        if (amount <= 0) {
            throw new IllegalArgumentException("loan amount can't be negative or zero");
        } else if (String.valueOf(loanID).length() != 8) {
            throw new IllegalArgumentException("account ID is not valid");
        } else if (String.valueOf(ownerPIN).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        } else if (grantedBy.length() != 7) {
            throw new IllegalArgumentException("employeeID is not valid");
        }
        interestChanges = new ArrayList<>();
        transactions = new ArrayList<>();
        this.loanID = loanID;
        this.ownerPIN = ownerPIN;
        this.amount = amount;
        this.interest = interest;
        this.grantedBy = grantedBy;
    }

    public List<InterestRateChange> getInterestChanges() {
        return Collections.unmodifiableList(interestChanges);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public String getLoanID() {
        return loanID;
    }

    public String getOwnerPIN() {
        return ownerPIN;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterest() {
        return interest;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    private void setAmount(double amount) {
        this.amount = amount;
    }

    private void setInterest(double interest) {
        this.interest = interest;
    }

    public void makeInterestChange(double interest, String timestamp, String grantedBy) {
        interestChanges.add(new InterestRateChange(interest, timestamp, grantedBy));
        this.interest = interest;
    }

    public void makeTransaction(double amount, String timestamp) {
        if (this.amount + amount < 0){
            throw new IllegalArgumentException("Can't pay more then the current loan sum");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Can't withdraw money from the loan");
        }
        transactions.add(new Transaction(amount, timestamp));
        this.amount += amount;
    }

    @Override
    public String toString() {
        return "ID: " + loanID + ", owner: " + ownerPIN + ", granted by: " + grantedBy +
                "\n amount: " + amount + ", interest: " + interest;
    }
}
