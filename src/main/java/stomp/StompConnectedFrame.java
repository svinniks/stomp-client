package stomp;

public class StompConnectedFrame extends StompFrame {

    @Override
    public Command getCommand() {
        return Command.CONNECTED;
    }

    @Override
    public void validate() {

        String version = getRequiredHeader("version");

        if (!version.equals("1.1"))
            throw new StompProtocolException(String.format("Unsupported STOMP version %s!", version));

    }

}
