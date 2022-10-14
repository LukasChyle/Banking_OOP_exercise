public record InterestRateChange(double interest, String timestamp, String grantedBy) {

    @Override
    public String toString() {
        return "  interest set to " + (interest * 100) + "% , granted by " + grantedBy + " , " + timestamp + "  ";
    }
}
