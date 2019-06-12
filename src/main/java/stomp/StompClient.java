package stomp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class StompClient {

    private final StompTransport transport;
    private final BlockingQueue<String> incomingFrameQueue;
    private final Map<UUID, Consumer<StompMessageFrame>> subscriptions;

    private Thread pollThread;

    public StompClient(StompTransport transport) {

        assert transport != null : "Transport must not be null!";
        this.transport = transport;
        transport.setEventHandlers(this::receive, this::transportDisconnect);

        incomingFrameQueue = new LinkedBlockingDeque<>();
        subscriptions = new HashMap<>();

    }

    private void send(StompFrame frame) {
        transport.sendFrame(frame.build());
    }

    final protected void receive(String frame) {
        incomingFrameQueue.add(frame);
    }

    final protected void transportDisconnect() {
        if (pollThread != null)
            pollThread.interrupt();
    }

    private StompFrame poll() throws InterruptedException {
        return poll(Long.MAX_VALUE);
    }

    private StompFrame poll(long timeout) throws InterruptedException {
        String frame = incomingFrameQueue.poll(timeout, TimeUnit.SECONDS);
        return new StompFrameParser().parse(frame);
    }

    private void consume(StompMessageFrame message) {

        UUID subscriptionId = UUID.fromString(message.getHeader("subscription"));
        Consumer<StompMessageFrame> consumer = subscriptions.get(subscriptionId);

        if (consumer == null)
            throw new StompProtocolException(String.format("Unknown subscription ID %s", subscriptionId));

        try {
            consumer.accept(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void connect(Map<String, String> headers) {

        send(new StompConnectFrame(headers).setHeader("accept-version", "1.1"));
        StompFrame connectedFrame;

        try {
            connectedFrame = poll(10);
        } catch (InterruptedException ex) {
            throw new StompProtocolException("Connection timeout has occurred!");
        }

        if (!(connectedFrame instanceof StompConnectedFrame))
            throw new StompProtocolException(String.format("CONNECTED frame expected, %s received.", connectedFrame.getCommand()));

        pollThread = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {

                        StompFrame frame = poll();

                        if (frame instanceof StompMessageFrame)
                            consume((StompMessageFrame)frame);
                        else
                            throw new StompProtocolException(String.format("Unexpected frame %s!", frame.getCommand()));

                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }

        };

        pollThread.start();

    }

    public void connect() {
        connect(null);
    };

    public UUID subscribe(String destination, Consumer<StompMessageFrame> messageConsumer) {

        assert messageConsumer != null : "Message consumer must not be null!";

        UUID subscriptionId = UUID.randomUUID();
        subscriptions.put(subscriptionId, messageConsumer);

        send(new StompSubscribeFrame()
                .setHeader("destination", destination)
                .setHeader("id", subscriptionId.toString())
        );

        return subscriptionId;

    }

}
