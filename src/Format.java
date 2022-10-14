public class Format {

    public  String formatPIN(String input){
        input = input.trim();
        if (input.length() == 10) {
            input = input.substring(0, 6) + "-" + input.substring(6);
        }
        return input;
    }

    public  String formatID(String input) {
        input = input.trim();
        if (input.length() == 6) {
            input = input.substring(0, 4) + "-" + input.substring(4);
        }
        return input;
    }
}
