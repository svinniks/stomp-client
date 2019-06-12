package stomp;

public class StompDisconnectFrame extends StompFrame {

    @Override
    public Command getCommand() {
        return Command.DISCONNECT;
    }

    @Override
    public void validate() {
        getRequiredHeader("receipt");
        assertNoBody();
    }

}
