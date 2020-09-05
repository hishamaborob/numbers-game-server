package numbersgameserver;

import java.io.IOException;
import java.net.Socket;

public interface ClientHandlerFactory {

    Runnable getNewHandler(Socket clientSocket) throws IOException;
}
