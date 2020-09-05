package numbersgameserver.adapter.handler;

import numbersgameserver.Listener;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Sends messages to client.
 */
public class ClientMessageListener implements Listener {

    private static final Logger LOGGER = Logger.getLogger(ClientMessageListener.class);

    private final DataOutputStream userOutput;

    public ClientMessageListener(DataOutputStream userOutput) {
        this.userOutput = userOutput;
    }

    @Override
    public void onMessage(final String message) {

        try {
            userOutput.write((message + "\n").getBytes(StandardCharsets.US_ASCII));
            userOutput.flush();
        } catch (Exception ioe) {
            LOGGER.warn("Could not send message", ioe);
        }
    }
}
