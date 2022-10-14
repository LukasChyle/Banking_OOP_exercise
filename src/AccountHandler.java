import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountHandler {

    private final List<Account> accounts;
    private final MainDialog mainDialog;
    private final CreateAccountID createID;
    private final ScrollPaneMessage spm;
    private final SimpleDateFormat sdf;

    public AccountHandler(MainDialog mainDialog, CreateAccountID createID,
                          ScrollPaneMessage spm, SimpleDateFormat sdf) {
        accounts = new ArrayList<>();
        this.mainDialog = mainDialog;
        this.spm = spm;
        this.sdf = sdf;
        this.createID = createID;
    }

    public void getCustomerAccounts(Customer customer) {
        List<Account> accountList = getAllAccountsWithPIN(customer.getPIN());
        if (accountList.isEmpty()) {
            JOptionPane.showMessageDialog(null, customer.getPIN() + " don't have any accounts");
        }

        Account[] accountArray = accountList.toArray(Account[]::new);

        Account account = (Account) JOptionPane.showInputDialog(null, "Choose account",
                "Accounts of " + customer.getPIN(), JOptionPane.QUESTION_MESSAGE, null,
                accountArray, accountArray[0]);
        if (account == null) {
            return;
        }
        mainDialog.accountDialog(account);
    }

    public void createAccount(Customer customer) {
        try {
            String accountID = setAccount(customer);
            JOptionPane.showMessageDialog(null, "new account created with ID: " + accountID);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "account not created: " + e);
        }
    }

    private String setAccount(Customer customer) throws IllegalArgumentException {
        String accountID = createID.setID(mainDialog.getAccountHandler(), mainDialog.getLoanHandler());
        java.util.Date date = new java.util.Date();
        accounts.add(new Account(accountID, customer.getPIN(), sdf.format(date)));
        return accountID;
    }

    public void makeTransaction(Account account, boolean deposit) {
        String type = "deposit";
        if (!deposit) {
            type = "withdraw";
        }
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter sum you want to " + type);
            if (input == null) {
                return;
            }

            java.util.Date date = new java.util.Date(); //updates the timeStamp

            try {
                if (deposit) {
                    account.makeTransaction(Double.parseDouble(input), sdf.format(date)); // adds
                    return;
                }
                account.makeTransaction(-Double.parseDouble(input), sdf.format(date)); // subtracts
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "input not valid");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }
    }

    public void printTransactions(Account account) {
        List<Transaction> transactions = account.getTransactions();
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no transactions");
            return;
        }
        spm.printMessage(sb.toString(), "All transactions", 400);
    }

    public Account getAccountWithID(String accountID) {
        for (Account a : accounts) {
            if (a.getAccountID().equals(accountID)) {
                return a;
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

    public double getAccountsSum(String pin){
        double balanceSum = 0;
        for (Account a : accounts) {
            if (a.getOwnerPIN().equals(pin)) {
                balanceSum += a.getBalance();
            }
        }
        return balanceSum;
    }

    public void setAccountInterest() {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Current interest " + (Account.getInterest() * 100) + "%, set new interest in %");
            if (input == null) {
                return;
            }
            try {
                Account.setInterest(Double.parseDouble(input) / 100);
                JOptionPane.showMessageDialog(null, "interest set to " + input + "%");
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "interest input not valid");
            }
        }
    }
}
