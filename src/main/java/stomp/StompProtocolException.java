package stomp;

public class StompProtocolException extends RuntimeException {

    public StompProtocolException() {
    }

    public StompProtocolException(String message) {
        super(message);
    }

    public StompProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

}
