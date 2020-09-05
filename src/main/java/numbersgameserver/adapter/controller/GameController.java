package numbersgameserver.adapter.controller;

import numbersgameserver.Controller;
import numbersgameserver.Listener;
import numbersgameserver.domain.model.game.Addition;
import numbersgameserver.projection.ClientMessageProducer;
import numbersgameserver.projection.GamesRepository;
import numbersgameserver.service.game.GameService;
import numbersgameserver.service.game.command.NumberAdditionCommand;
import numbersgameserver.service.game.command.StartGameCommand;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Game controller. Keep clients in order by checking queues,
 * parsing and passing commands to GameService.
 * Register and unregister clients messages listeners.
 */
public class GameController implements Controller {

    private static final Logger LOGGER = Logger.getLogger(GameController.class);

    private static final String USER_MESSAGE = "Enter a number greater or equal to 4";

    private GamesRepository gamesRepository;
    private GameService gameService;
    private ClientMessageProducer clientMessageProducer;

    public GameController(
            GamesRepository gamesRepository, GameService gameService, ClientMessageProducer clientMessageProducer) {

        this.gamesRepository = gamesRepository;
        this.gameService = gameService;
        this.clientMessageProducer = clientMessageProducer;
    }

    @Override
    public void registerListener(UUID clientId, Listener listener) {

        if (!clientMessageProducer.isClientSubscribingToMessages(clientId)) {
            clientMessageProducer.addClientListener(clientId, listener);
            clientMessageProducer.sendMessage(clientId, USER_MESSAGE);
        }
    }

    @Override
    public void unregisterListener(UUID clientId) {

        clientMessageProducer.removeClientListener(clientId);
    }

    @Override
    public void postCommand(UUID clientId, String command) {

        if (!gamesRepository.isClientAddedToGame(clientId)) {
            startGame(clientId, command);
        } else {
            gameMove(clientId, command);
        }
    }

    private void startGame(UUID clientId, String command) {

        try {
            long number = Long.parseLong(command);
            if (number < 4) {
                clientMessageProducer.sendMessage(clientId, USER_MESSAGE);
                return;
            }
            gameService.process(new StartGameCommand(clientId, number));
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            clientMessageProducer.sendMessage(clientId, USER_MESSAGE);
        }
    }

    private void gameMove(UUID clientId, String command) {

        try {
            int number = Integer.parseInt(command);
            Addition addition;
            if ((addition = Addition.get(number)) == null) {
                clientMessageProducer.sendMessage(clientId, "Add 0 , 1 , or -1");
                return;
            }
            gameService.process(new NumberAdditionCommand(
                    UUID.fromString(gamesRepository.getClientGame(clientId).getGameId()),
                    clientId, addition
            ));
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            clientMessageProducer.sendMessage(clientId, "Add number 0 , 1 , or -1 or wait for your turn");
        }
    }
}
