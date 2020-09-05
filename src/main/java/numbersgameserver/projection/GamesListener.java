package numbersgameserver.projection;

import com.google.common.eventbus.Subscribe;
import numbersgameserver.domain.model.game.event.GameStartEvent;
import numbersgameserver.domain.model.game.event.NumberAdditionEvent;

import java.util.UUID;

/**
 * Listening to games events, updates some data projection, push messages to clients.
 */
public class GamesListener {

    private GamesRepository gamesRepository;
    private ClientMessageProducer clientMessageProducer;

    public GamesListener(GamesRepository gamesRepository, ClientMessageProducer clientMessageProducer) {
        this.gamesRepository = gamesRepository;
        this.clientMessageProducer = clientMessageProducer;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(GameStartEvent event) {

        GameProjection gameProjection = new GameProjection(
                event.getAggregateId().toString(), event.getPlayerId().toString(), event.getCurrentNumber(), 0);
        gamesRepository.addGame(event.getAggregateId(), gameProjection);
        gamesRepository.addClientGame(event.getPlayerId(), gameProjection);
        clientMessageProducer.sendMessage(event.getPlayerId(), "Game started. Wait for Rival's move");
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(NumberAdditionEvent event) {

        GameProjection gameProjection = gamesRepository.getGame(event.getAggregateId());
        gameProjection.setCurrentNumber(event.getCurrentNumber());
        gameProjection.setLastAddition(event.getAddition());
        UUID playerId = UUID.fromString(gameProjection.getPlayerId());
        clientMessageProducer.sendMessage(playerId,
                (event.getPlayerId().equals(playerId) ? "You played " : "Your rival played ") +
                        gameProjection.getLastAddition() + " resulted number is " + gameProjection.getCurrentNumber());

        if (event.isGameFinished()) {
            clientMessageProducer.sendMessage(playerId,
                    event.getPlayerId().equals(playerId) ? "You're Winner" : "You Lost");

            gamesRepository.removeClientGame(playerId);
            gamesRepository.removeGame(event.getAggregateId());
            clientMessageProducer.sendMessage(playerId, "Play new game. Enter a number greater or equal to 4");
        }
    }

}
