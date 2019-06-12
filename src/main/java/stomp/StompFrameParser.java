package stomp;

import stomp.StompFrame.Command;

import static stomp.StompFrame.Command.*;
import static stomp.StompFrameParser.State.*;

public class StompFrameParser {

    private State state;
    private StompFrame frame;
    private StringBuilder bodyBuilder;

    protected enum State {
        R_COMMAND,
        R_HEADER,
        R_BODY,
        R_END
    }

    private void rCommand(String line) {

        Command command;

        try {
            command = Command.valueOf(line);
        } catch (IllegalArgumentException ex) {
            throw new StompProtocolException(String.format("Unsupported command %s!", line));
        }

        if (command == CONNECT)
            frame = new StompConnectFrame();
        else if (command == CONNECTED)
            frame = new StompConnectedFrame();
        else if (command == SUBSCRIBE)
            frame = new StompSubscribeFrame();
        else if (command == MESSAGE)
            frame = new StompMessageFrame();
        else
            throw new StompProtocolException(String.format("Unsupported command %s!", line));

        state = R_HEADER;

    }

    private void rHeader(String line) {

        if (line.length() == 0) {
            state = R_BODY;
            bodyBuilder = new StringBuilder();
        } else {

            String[] parts = line.split(":");

            if (parts.length != 2)
                throw new StompProtocolException("Invalid header format!");
            else if (parts[0].length() == 0)
                throw new StompProtocolException("Empty header name!");

            frame.setHeader(
                    StompFrame.unescapeString(parts[0]),
                    StompFrame.unescapeString(parts[1])
            );

        }

    }

    public void rBody(String line) {

        int nulPosition = line.indexOf('\0');

        if (nulPosition == -1)
            bodyBuilder.append(line);
        else if (nulPosition == line.length() - 1) {
            bodyBuilder.append(line, 0, line.length() - 1);
            frame.setBody(bodyBuilder.toString());
            state = R_END;
        } else
            throw new StompProtocolException("Unexpected characters beyond frame terminator!");

    }

    private void rEnd(String line) {
        if (line.length() > 0)
            throw new StompProtocolException("Unexpected characters beyond frame terminator!");
    }

    public StompFrame parse(String frameString) {

        if (frameString == null)
            throw new StompProtocolException("Null frame received!");

        String[] lines = frameString.split("\n");
        state = R_COMMAND;

        for (String line : lines) {
            if (state == R_COMMAND)
                rCommand(line);
            else if (state == R_HEADER)
                rHeader(line);
            else if (state == R_BODY)
                rBody(line);
            else if (state == R_END)
                rEnd(line);
        }

        if (state != R_END)
            throw new StompProtocolException("Unexpected end of the frame!");

        frame.validate();
        return frame;

    }

}
