public record InterestRateChange(double interest, String timestamp, String grantedBy) {

    @Override
    public String toString() {
        return "  interest set to " + interest + "% , granted by " + grantedBy + " , " + timestamp + "  ";
    }
}
