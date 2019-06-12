package stomp;

import java.util.Map;

public class StompConnectFrame extends StompFrame {

    public StompConnectFrame() {
    }

    public StompConnectFrame(Map<String, String> headers) {
        super(headers);
    }

    @Override
    public void validate() {

        assertNoBody();

        String acceptVersion = getRequiredHeader("accept-version");

        if (!acceptVersion.equals("1.1"))
            throw new StompProtocolException(String.format("Unsupported accept version %s!", acceptVersion));

    }

    @Override
    public Command getCommand() {
        return Command.CONNECT;
    }

}
