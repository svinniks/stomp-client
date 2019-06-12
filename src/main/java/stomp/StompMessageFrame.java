package stomp;

public class StompMessageFrame extends StompFrame {

    @Override
    public Command getCommand() {
        return null;
    }

    @Override
    public void validate() {
        getRequiredHeader("destination");
        getRequiredHeader("subscription");
        getRequiredHeader("message-id");
    }

}
