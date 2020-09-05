package numbersgameserver.service.game;

import com.google.common.eventbus.EventBus;
import numbersgameserver.domain.model.Event;
import numbersgameserver.domain.model.EventStore;
import numbersgameserver.domain.model.game.Game;
import numbersgameserver.service.game.command.NumberAdditionCommand;
import numbersgameserver.service.game.command.StartGameCommand;
import numbersgameserver.service.game.exception.GameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.UUID.randomUUID;

/**
 * Process commands, load up the current state and publish new events through event bus.
 */
public class GameService {

    private EventStore eventStore;
    private EventBus eventBus;


    public GameService(EventStore eventStore, EventBus eventBus) {

        this(eventStore);
        this.eventBus = checkNotNull(eventBus);
    }

    public GameService(EventStore eventStore) {

        this.eventStore = checkNotNull(eventStore);
    }

    public Optional<Game> loadGame(UUID gameId) {

        List<Event> eventStream = eventStore.load(gameId);
        if (eventStream.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Game(gameId, eventStream));
    }

    public Game process(StartGameCommand command) {

        Game game = new Game(randomUUID(), command.getChallengerId(), command.getCurrentNumber());
        storeEvents(game);
        publishEvents(game);
        return game;
    }

    public Game process(NumberAdditionCommand command) throws GameNotFoundException {

        return process(command.getGameId(), game ->
                game.playNumber(command.getPlayerId(), command.getAddition()));
    }

    private Game process(UUID gameId, Consumer<Game> consumer) throws GameNotFoundException {

        Optional<Game> gameOptional = loadGame(gameId);
        Game game = gameOptional.orElseThrow(() -> new GameNotFoundException(gameId));
        consumer.accept(game);
        storeEvents(game);
        publishEvents(game);
        return game;
    }

    private void storeEvents(Game game) {

        eventStore.store(game.getId(), game.getNewEvents());
    }

    private void publishEvents(Game game) {

        if (eventBus != null) {
            game.getNewEvents().forEach(eventBus::post);
        }
    }
}
