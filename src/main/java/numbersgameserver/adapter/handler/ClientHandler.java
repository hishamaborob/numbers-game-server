package numbersgameserver.adapter.handler;

import numbersgameserver.Controller;
import numbersgameserver.Listener;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

/**
 * Handles client socket connection and passes command messages to the controller.
 * Also creates message listeners, register and unregister them.
 */
public class ClientHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class);

    private final Socket clientSock;
    private final UUID clientId;
    private final Controller controller;

    public ClientHandler(final Socket clientSocket, Controller controller) {
        this.clientSock = clientSocket;
        this.controller = controller;
        this.clientId = UUID.randomUUID();
    }

    @Override
    public void run() {

        try (this.clientSock;
             DataOutputStream userOutput = new DataOutputStream(this.clientSock.getOutputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(this.clientSock.getInputStream()));
        ) {
            Listener clientMessageListener = new ClientMessageListener(userOutput);
            controller.registerListener(clientId, clientMessageListener);
            while (true) {
                String line = userInput.readLine();
                if (line != null) {
                    controller.postCommand(clientId, line);
                }
            }
        } catch (Exception ioe) {
            LOGGER.warn("Lost connection", ioe);
        }
        controller.unregisterListener(clientId);
    }
}