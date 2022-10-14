import javax.swing.*;

public class CreateName {

    public String setName(String type) {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "enter " + type);
            if (name == null) {
                throw new IllegalArgumentException("canceled");
            }
            name = name.trim();
            if (!name.chars().allMatch(Character::isLetter) || name.isBlank()) {
                JOptionPane.showMessageDialog(null, type + " is not valid");
                continue;
            }
            return name;
        }
    }
}
