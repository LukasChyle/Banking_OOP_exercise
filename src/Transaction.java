public record Transaction(double amount, String timestamp) {

    @Override
    public String toString() {
        return "  " +amount + " , timestamp: " + timestamp + "  ";
    }
}
