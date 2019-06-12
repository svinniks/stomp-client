package test;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import stomp.StompTransport;

import java.net.URI;
import java.util.function.Consumer;

public class WebSocketStompTransport extends WebSocketClient implements StompTransport {

    private Consumer<String> frameConsumer;
    private Runnable disconnectHandler;

    public WebSocketStompTransport(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        frameConsumer.accept(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        disconnectHandler.run();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void setEventHandlers(Consumer<String> receiveFrame, Runnable disconnect) {
        this.frameConsumer = receiveFrame;
        this.disconnectHandler = disconnect;
    }

    @Override
    public void sendFrame(String frame) {
        send(frame);
    }

}
