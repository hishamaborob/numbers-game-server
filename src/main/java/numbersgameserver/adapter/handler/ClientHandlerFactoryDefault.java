package numbersgameserver.adapter.handler;

import numbersgameserver.ClientHandlerFactory;
import numbersgameserver.Controller;

import java.net.Socket;

/**
 * Creates new handler thread per client connection.
 */
public class ClientHandlerFactoryDefault implements ClientHandlerFactory {

    private Controller controller;

    public ClientHandlerFactoryDefault(Controller controller) {
        this.controller = controller;
    }

    @Override
    public Runnable getNewHandler(Socket clientSocket) {

        return new ClientHandler(clientSocket, controller);
    }
}
