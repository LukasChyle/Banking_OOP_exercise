import java.io.Serializable;

public record Transaction(double amount, String id, String timestamp) implements Serializable {

    @Override
    public String toString() {
        return "  Sum: " + amount + "  -  Acc: " + id + "  -  " + timestamp + "  ";
    }
}
