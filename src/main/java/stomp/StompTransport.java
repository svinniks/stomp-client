package stomp;

public interface StompTransport {
    void send(String frame);
    void receive(String frame);
}
