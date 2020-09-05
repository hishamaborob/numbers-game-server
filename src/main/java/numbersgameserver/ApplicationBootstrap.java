package numbersgameserver;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import numbersgameserver.adapter.engine.RandomGamePlayer;
import numbersgameserver.adapter.handler.ClientHandlerFactoryDefault;
import numbersgameserver.adapter.controller.GameController;
import numbersgameserver.adapter.store.InMemoryEventStore;
import numbersgameserver.domain.model.EventStore;
import numbersgameserver.projection.*;
import numbersgameserver.service.game.GameService;
import org.apache.log4j.Logger;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * App dependencies builder and bootstrap.
 */
public class ApplicationBootstrap {

    private static final Logger LOGGER = Logger.getLogger(ApplicationBootstrap.class);

    public static void main(String[] args) {

        GamesRepository gamesRepository = new InMemoryGamesRepository();
        EventBus eventBus = new AsyncEventBus(newSingleThreadExecutor());
        ClientMessageProducer clientMessageProducer = new ClientMessageProducerAsync();
        GamesListener gamesListener = new GamesListener(gamesRepository, clientMessageProducer);
        eventBus.register(gamesListener);
        EventStore eventStore = new InMemoryEventStore();
        GameService gameService = new GameService(eventStore, eventBus);
        RandomGamePlayer randomGamePlayer = new RandomGamePlayer(gameService);
        eventBus.register(randomGamePlayer);
        Controller controller = new GameController(gamesRepository, gameService, clientMessageProducer);
        ClientHandlerFactory clientHandlerFactory = new ClientHandlerFactoryDefault(controller);
        ThreadPoolTCPEchoServer server = new ThreadPoolTCPEchoServer(8080, clientHandlerFactory);
        server.start();
        try {
            server.join();
            LOGGER.info("Completed shutdown.");
        } catch (InterruptedException e) {
            // ignore exception
            LOGGER.warn("Interrupted before accept thread completed.");
            System.exit(1);
        }
    }
}
