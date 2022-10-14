import java.io.Serializable;

public record Transaction(double amount, String timestamp) implements Serializable {

    @Override
    public String toString() {
        return "  " +amount + " , timestamp: " + timestamp + "  ";
    }
}
