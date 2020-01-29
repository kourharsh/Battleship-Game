public class TimeException extends Exception {

    public TimeException(String time) {
        super(String.valueOf(time));
    }
}