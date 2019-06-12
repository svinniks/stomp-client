package stomp;

import java.util.function.Consumer;

public interface StompTransport {
    void sendFrame(String frame);
    void setEventHandlers(Consumer<String> receiveFrame, Runnable disconnect);
}
