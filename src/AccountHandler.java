import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AccountHandler {

    private final SimpleDateFormat sdf;

    public AccountHandler() {
        sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    }

    public void dialog(Account account) {
        String[] options = {"Deposit", "Withdraw", "Transaction history", "Return"};
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, account, "Account",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 3 -> run = false;
                case 0 -> makeTransaction(account, true);
                case 1 -> makeTransaction(account, false);
                case 2 -> printTransactions(account);
            }
        }
    }

    private void makeTransaction(Account account, boolean deposit) {
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

    private void printTransactions(Account account) {
        List<Transaction> transactions = account.getTransactions();
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "there is no transactions");
            return;
        }
        ScrollPaneMessage.printMessage(sb.toString(), "All transactions", 300);
    }
}
