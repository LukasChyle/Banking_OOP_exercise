import javax.swing.*;

public class CreatePIN {

    public static String setPIN(boolean customer, CustomerHandler customerHandler, EmployeeHandler employeeHandler) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "enter 10 digit personal identity number");
            if (input == null) {
                throw new IllegalArgumentException("canceled");
            }
            input = input.trim();
            if (input.length() == 10 && input.chars().allMatch(Character::isDigit)) {
                input = input.substring(0, 6) + "-" + input.substring(6);
            }
            if (input.length() == 11 && input.substring(0, 6).chars().allMatch(Character::isDigit) &&
                    input.charAt(6) == '-' && input.substring(7).chars().allMatch(Character::isDigit)) {
                if (customer && customerHandler.getCustomerWithPIN(input) == null) {
                    return input;
                } else if (!customer && employeeHandler.getEmployeeWithPIN(input) == null) {
                    return input;
                }
                JOptionPane.showMessageDialog(null, "this personal identity number is already in use");
                continue;
            }
            JOptionPane.showMessageDialog(null, "personal identity number is not valid");
        }
    }
}
