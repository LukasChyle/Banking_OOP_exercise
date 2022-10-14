import java.io.Serializable;

public class Account implements Serializable {

    private final String accountID;
    private double balance;
    private static double interest;
    private final String ownerPIN;
    private final String dateCreated;

    public Account(String accountID, String ownerPIN, String timestamp) {
        if (String.valueOf(accountID).length() != 8) {
            throw new IllegalArgumentException("account ID is not valid");
        } else if (String.valueOf(ownerPIN).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        }
        this.accountID = accountID;
        this.ownerPIN = ownerPIN;
        dateCreated = timestamp;
    }

    public static void setInterest(double interest) {
        Account.interest = interest;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getOwnerPIN() {
        return ownerPIN;
    }

    public double getBalance() {
        return balance;
    }

    public static double getInterest() {
        return interest;
    }

    public String getTimeCreated() {
        return dateCreated;
    }

    public void makeTransaction(double amount) {
        if ((amount + balance) < 0) {
            throw new IllegalArgumentException("Can't withdraw more then the account balance");
        }
        balance += amount;
    }

    @Override
    public String toString() {
        return "  ID: " + accountID + "  -  owner: " + ownerPIN + "  -  balance: " + balance +
                "  -  interest: " + (interest * 100) + "%  -  created: " + dateCreated + "  ";
    }
}