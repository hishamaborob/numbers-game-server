package numbersgameserver.service.game;

import numbersgameserver.adapter.store.InMemoryEventStore;
import numbersgameserver.domain.model.EventStore;
import numbersgameserver.domain.model.game.Addition;
import numbersgameserver.domain.model.game.Game;
import numbersgameserver.domain.model.game.event.GameStartEvent;
import numbersgameserver.domain.model.game.event.NumberAdditionEvent;
import numbersgameserver.domain.model.game.exception.GameFinishedException;
import numbersgameserver.domain.model.game.exception.WrongPlayerOnMoveException;
import numbersgameserver.service.game.command.NumberAdditionCommand;
import numbersgameserver.service.game.command.StartGameCommand;
import numbersgameserver.service.game.exception.GameNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

public class GameServiceTest {

    private static final UUID testGameId = randomUUID();
    private static final UUID testChallenger = randomUUID();
    private static final UUID testRivalId = randomUUID();

    private EventStore eventStore;
    private GameService gameService;

    @Before
    public void setUp() throws Exception {

        eventStore = new InMemoryEventStore();
        gameService = new GameService(eventStore);
    }

    @Test(expected = NullPointerException.class)
    public void provideNullEventStore() {
        GameService gameService = new GameService(null, null);
    }

    @Test
    public void loadGame() {

        eventStore.store(testGameId, Collections.singletonList(new GameStartEvent(testGameId, testChallenger, 67)));
        Optional<Game> gameOptionalShouldEmpty = gameService.loadGame(randomUUID());
        assertFalse(gameOptionalShouldEmpty.isPresent());
        Optional<Game> gameOptional = gameService.loadGame(testGameId);
        assertTrue(gameOptional.isPresent());
        assertTrue(gameOptional.get().getNewEvents().isEmpty());
    }

    @Test(expected = GameNotFoundException.class)
    public void playOnNonExistingGame() {

        gameService.process(new NumberAdditionCommand(randomUUID(), testChallenger, Addition.ZERO));
    }

    @Test(expected = GameFinishedException.class)
    public void numberAdditionToFinishedGame() {

        storeFinishedGame();
        gameService.process(new NumberAdditionCommand(testGameId, testChallenger, Addition.ZERO));
    }

    @Test(expected = WrongPlayerOnMoveException.class)
    public void wrongPlayerOnMove() {

        storeUnfinishedGame();
        gameService.process(new NumberAdditionCommand(testGameId, testRivalId, Addition.ZERO));
    }

    @Test
    public void completeGameServiceFlowTest() {

        UUID gameId;
        Game game = gameService.process(new StartGameCommand(testChallenger, 56));
        assertNotNull(game);
        assertNotNull(gameId = game.getId());
        assertEquals(56, game.getCurrentNumber());
        assertFalse(game.isPlayerOnMove());
        game = gameService.process(new NumberAdditionCommand(gameId, testRivalId, Addition.ZERO));
        assertEquals(56, game.getCurrentNumber());
        assertEquals(Addition.ZERO, game.getLastAddition());
        game = gameService.process(new NumberAdditionCommand(gameId, testChallenger, Addition.MINUS_ONE));
        assertEquals(56, game.getCurrentNumber());
        assertEquals(Addition.MINUS_ONE, game.getLastAddition());
        game = gameService.process(new NumberAdditionCommand(gameId, testRivalId, Addition.ONE));
        assertEquals(19, game.getCurrentNumber());
        assertEquals(Addition.ONE, game.getLastAddition());
        game = gameService.process(new NumberAdditionCommand(gameId, testChallenger, Addition.MINUS_ONE));
        assertEquals(6, game.getCurrentNumber());
        game = gameService.process(new NumberAdditionCommand(gameId, testRivalId, Addition.ZERO));
        assertEquals(2, game.getCurrentNumber());
        game = gameService.process(new NumberAdditionCommand(gameId, testChallenger, Addition.ONE));
        assertEquals(1, game.getCurrentNumber());
        assertTrue(game.isFinished());
    }

    private void storeFinishedGame() {
        eventStore.store(testGameId, Arrays.asList(
                new GameStartEvent(testGameId, testChallenger, 2),
                new NumberAdditionEvent(testGameId, testRivalId, Addition.ONE, 1, true, true)));
    }

    private void storeUnfinishedGame() {
        eventStore.store(testGameId, Arrays.asList(
                new GameStartEvent(testGameId, testChallenger, 2),
                new NumberAdditionEvent(testGameId, testRivalId, Addition.ZERO, 2, false, true)));
    }
}