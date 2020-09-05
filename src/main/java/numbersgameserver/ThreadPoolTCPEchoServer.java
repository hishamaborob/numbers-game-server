package numbersgameserver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multi clients TCP server
 */
public class ThreadPoolTCPEchoServer extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ThreadPoolTCPEchoServer.class);

    private ExecutorService workers = Executors.newCachedThreadPool();

    private ServerSocket listenSocket;

    private ClientHandlerFactory clientHandlerFactory;

    private int socketAcceptTimeoutMilli = 5000;

    private volatile boolean run = true;

    public ThreadPoolTCPEchoServer(
            final int port, ClientHandlerFactory clientHandlerFactory,
            ExecutorService executorService, final int socketAcceptTimeoutMilli) {

        this(port, clientHandlerFactory);
        workers = executorService;
        this.socketAcceptTimeoutMilli = socketAcceptTimeoutMilli;
    }

    public ThreadPoolTCPEchoServer(
            final int port, ExecutorService executorService, ClientHandlerFactory clientHandlerFactory) {

        this(port, clientHandlerFactory);
        workers = executorService;
    }

    public ThreadPoolTCPEchoServer(final int port, ClientHandlerFactory clientHandlerFactory) {

        this.clientHandlerFactory = clientHandlerFactory;
        Runtime.getRuntime().addShutdownHook(new Thread(ThreadPoolTCPEchoServer.this::shutdown));
        try {
            this.listenSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    @Override
    public void run() {

        try {
            this.listenSocket.setSoTimeout(socketAcceptTimeoutMilli);
        } catch (SocketException e1) {
            LOGGER.warn("Unable to set acceptor timeout value.");
        }
        LOGGER.info("Accepting incoming connections on port " + this.listenSocket.getLocalPort());
        while (this.run) {
            try {
                final Socket clientSocket = this.listenSocket.accept();
                LOGGER.info("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                Runnable clientHandler = clientHandlerFactory.getNewHandler(clientSocket);
                this.workers.execute(clientHandler);

            } catch (SocketTimeoutException te) {
                // ignore this
            } catch (IOException ioe) {
                LOGGER.error("Exception occurred while handling client request: " + ioe.getMessage(), ioe);
            }
        }
        try {
            this.listenSocket.close();
        } catch (IOException ioe) {
            LOGGER.warn(ioe);
        }
        LOGGER.info("Stopped accepting incoming connections.");
    }

    public void shutdown() {
        LOGGER.info("Shutting down the server.");
        this.run = false;
        this.workers.shutdownNow();
        try {
            this.join();
        } catch (InterruptedException e) {
            // ignore this
            // ignore exception
        }
    }
}