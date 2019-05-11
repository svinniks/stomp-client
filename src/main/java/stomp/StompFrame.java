package stomp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class StompFrame {

    public static String escapeString(String value) {
        return value.replaceAll("\n", "\\n");
    };

    public enum Command {
        CONNECT,
        CONNECTED,
        SUBSCRIBE,
        UNSUBSCRIBE,
        MESSAGE,
        DISCONNECT
    }

    private final Map<String, String> headers;

    public StompFrame() {
        headers = new HashMap<>();
    }

    public abstract Command getCommand();
    public abstract String getBody();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String serialize() {

        final StringBuilder frameBuilder = new StringBuilder();

        frameBuilder
                .append(getCommand())
                .append('\n');

        headers.forEach((name, value) -> {
            frameBuilder
                    .append(escapeString(name))
                    .append(':')
                    .append(escapeString(value))
                    .append('\n');
        });

        frameBuilder
                .append('\n')
                .append(escapeString(getBody()))
                .append('\0');

        return frameBuilder.toString();

    }

}
