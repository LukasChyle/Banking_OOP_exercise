import java.io.Serializable;

public record InterestRateChange(String id, double interest, String timestamp, String grantedBy) implements Serializable {

    @Override
    public String toString() {
        return "  Acc: " + id + "  -  interest: " + (interest * 100) + "%  -  granted by: " + grantedBy + "  -  " + timestamp + "  ";
    }
}
