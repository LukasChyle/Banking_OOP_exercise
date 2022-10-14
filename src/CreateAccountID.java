import java.util.Random;

public class CreateAccountID {

    public static String setID(AccountHandler accountHandler, LoanHandler loanHandler) {
        Random rnd = new Random();
        while (true) {
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                id.append(rnd.nextInt(10));
            }
            if (accountHandler.getAccountWithID(id.toString()) == null && loanHandler.getLoanWithID(id.toString()) == null) {
                return id.toString();
            }
        }
    }
}
