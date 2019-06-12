package stomp;

public class StompSubscribeFrame extends StompFrame {

    @Override
    public Command getCommand() {
        return Command.SUBSCRIBE;
    }

    @Override
    public void validate() {
        getRequiredHeader("id");
        getRequiredHeader("destination");
        assertNoBody();
    }

}
