import javax.swing.*;

public class EmployeeHandler {

    private final Data data;

    public EmployeeHandler(Data data) {
        this.data = data;
    }

    public void dialog(Employee employee){

        String[] options = {"Change salary", "Loans granted", };
        String message = employee + "\nSalary: " + employee.getSalary();
        boolean run = true;

        while (run) {
            int action = JOptionPane.showOptionDialog(null, message, "Employee",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);

            switch (action) {
                case -1, 6 -> run = false;

            }
        }
    }
}
