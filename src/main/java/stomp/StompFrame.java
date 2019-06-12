package stomp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class StompFrame {

    public static String escapeString(String value) {
        return value;
    };

    public static String unescapeString(String value) {
        return value;
    }

    public enum Command {
        CONNECT,
        CONNECTED,
        SUBSCRIBE,
        UNSUBSCRIBE,
        MESSAGE,
        DISCONNECT
    }

    private final Map<String, String> headers;
    private String body;

    public StompFrame() {
        headers = new HashMap<>();
    }

    public StompFrame(Map<String, String> headers) {

        this();

        if (headers != null)
            this.headers.putAll(headers);

    }

    public abstract Command getCommand();
    public abstract void validate();

    protected void assertNoBody() {
        if (body != null && body.length() > 0)
            throw new StompProtocolException(String.format("%s frames can't have body!", getCommand()));
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequiredHeader(String name) {

        String value = headers.get(name);

        if (value == null || value.isEmpty())
            throw new StompProtocolException(String.format("Required header \"%s\" is missing!", name));

        return value;

    }

    public String getBody() {
        return body;
    }

    public StompFrame setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public StompFrame setBody(String body) {
        this.body = body;
        return this;
    }

    public String build() {

        validate();

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
                .append(escapeString(body == null ? "" : body))
                .append('\0');

        return frameBuilder.toString();

    }

}
