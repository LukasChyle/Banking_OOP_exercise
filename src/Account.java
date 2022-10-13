import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final List<Transaction> transactions;
    private final String accountID;
    private double balance;
    private static double interest;

    private final String ownerPIN;

    public Account(String accountID, String ownerPIN) {
        if (String.valueOf(accountID).length() != 8) {
            throw new IllegalArgumentException("account ID is not valid");
        } else if (String.valueOf(ownerPIN).length() != 11) {
            throw new IllegalArgumentException("social security number is not valid");
        }
        transactions = new ArrayList<>();
        this.accountID = accountID;
        this.ownerPIN = ownerPIN;
    }

    public static void setInterest(double interest) {
        Account.interest = interest;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
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

    public void makeTransaction(double amount, String timestamp) {
        if ((amount + balance) < 0){
            throw new IllegalArgumentException("Can't withdraw more then the account balance");
        }
        balance += amount;
        transactions.add(new Transaction(amount, timestamp));
    }

    @Override
    public String toString() {
        return "ID: " + accountID + ", owner: " + ownerPIN +"\nbalance: " + balance + ", interest: " + (interest*100) + "%" ;
    }
}