import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loan implements Serializable {

    private final String loanID;
    private double amount;
    private double interest;
    private final String grantedBy;
    private final String ownerPIN;
    private final String dateCreated;

    public Loan(String loanID, String ownerPIN, double amount, double interest, String grantedBy, String timestamp) {
        if (amount <= 0) {
            throw new IllegalArgumentException("loan amount can't be negative or zero");
        } else if (String.valueOf(loanID).length() != 8) {
            throw new IllegalArgumentException("account ID is not valid");
        } else if (String.valueOf(ownerPIN).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        } else if (grantedBy.length() != 7) {
            throw new IllegalArgumentException("employeeID is not valid");
        }
        this.loanID = loanID;
        this.ownerPIN = ownerPIN;
        this.amount = amount;
        this.interest = interest;
        this.grantedBy = grantedBy;
        dateCreated = timestamp;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void makeInterestChange(double interest) {
        this.interest = interest;
    }

    public void makePayment(double amount) {
        if ((this.amount + amount) < 0) {
            throw new IllegalArgumentException("Can't pay more then the current loan sum");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Can't withdraw money from the loan");
        }
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return "  ID: " + loanID + "  -  owner: " + ownerPIN + "  -  granted by: " + grantedBy +
                "  -  amount: " + amount + "  -  interest: " + (interest * 100) + "%  -  created: " + dateCreated + "  ";
    }
}
