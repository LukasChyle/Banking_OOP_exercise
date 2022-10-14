import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountHandler {

    private List<Account> accounts;
    private List<Transaction> accountTransactions;
    private final MainDialog mainDialog;
    private final SimpleDateFormat sdf;

    public AccountHandler(MainDialog mainDialog, SimpleDateFormat sdf) {
        retrieveAccountList();
        retrieveTransactionList();
        retrieveAccountInterest();
        this.mainDialog = mainDialog;
        this.sdf = sdf;
    }

    private void storeAccountList() {
        ObjectFileStore.storeObjectList(accounts, "accounts");
    }

    @SuppressWarnings("unchecked")
    private void retrieveAccountList() {
        List<Account> accountList = (List<Account>) ObjectFileStore.retrieveObjectList("accounts");
        if (accountList != null) {
            accounts = accountList;
        } else {
            accounts = new ArrayList<>();
        }
    }

    private void storeTransactionList() {
        ObjectFileStore.storeObjectList(accountTransactions, "accountTransactions");
    }

    @SuppressWarnings("unchecked")
    private void retrieveTransactionList() {
        List<Transaction> transactionList = (List<Transaction>) ObjectFileStore.retrieveObjectList("accountTransactions");
        if (transactionList != null) {
            accountTransactions = transactionList;
        } else {
            accountTransactions = new ArrayList<>();
        }
    }


    public void getCustomerAccounts(Customer customer) {
        List<Account> accountList;
        try {
            accountList = getAllAccountsWithPIN(customer.getPIN());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "error: " + e);
            return;
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
        String accountID = CreateAccountID.setID(mainDialog.getAccountHandler(), mainDialog.getLoanHandler());
        java.util.Date date = new java.util.Date();
        accounts.add(new Account(accountID, customer.getPIN(), sdf.format(date)));
        storeAccountList();
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
                    account.makeTransaction(Double.parseDouble(input)); // adds
                    createTransaction(Double.parseDouble(input), account, sdf.format(date));
                    storeTransactionList();
                    return;
                }
                account.makeTransaction(-Double.parseDouble(input)); // subtracts
                createTransaction(Double.parseDouble(input), account, sdf.format(date));
                storeTransactionList();
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "input not valid");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }
    }

    public void printTransactions(Account account) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction t : accountTransactions) {
            if (t.id().equals(account.getAccountID())) {
                transactions.add(t);
            }
        }
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no transactions");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        ScrollPaneMessage.printMessage(sb.toString(), "All transactions", 400);
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

    public double getAccountsSum(String pin) {
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
                storeAccountInterest();
                storeAccountList();
                JOptionPane.showMessageDialog(null, "interest set to " + input + "%");
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "interest input not valid");
            }
        }
    }

    private void createTransaction(double amount, Account account, String timestamp) {
        accountTransactions.add(new Transaction(amount, account.getAccountID(), timestamp));
    }

    private void storeAccountInterest() {
        try (ObjectOutputStream objOPS = new ObjectOutputStream(new FileOutputStream("accountInterest.ser"))) {
            objOPS.writeDouble(Account.getInterest());
            objOPS.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private void retrieveAccountInterest() {
        try (ObjectInputStream objIPS = new ObjectInputStream(new FileInputStream("accountInterest.ser"))) {
            Account.setInterest(objIPS.readDouble());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
