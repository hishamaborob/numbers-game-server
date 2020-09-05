package numbersgameserver.domain.model.game;

import numbersgameserver.domain.model.Aggregate;
import numbersgameserver.domain.model.Event;
import numbersgameserver.domain.model.game.event.GameStartEvent;
import numbersgameserver.domain.model.game.event.NumberAdditionEvent;
import numbersgameserver.domain.model.game.exception.GameFinishedException;
import numbersgameserver.domain.model.game.exception.WrongPlayerOnMoveException;

import java.util.List;
import java.util.UUID;

/**
 * Game aggregate. Generates new events and aggregate events to form the current state of the game.
 */
public class Game extends Aggregate {

    private UUID playerId;
    private long currentNumber;
    private boolean isFinished;
    private boolean isPlayerOnMove;
    private Addition lastAddition;

    public Game(UUID gameId, UUID challengerId, long currentNumber) {
        super(gameId);
        GameStartEvent gameStartEvent = new GameStartEvent(gameId, challengerId, currentNumber);
        applyNewEvent(gameStartEvent);
    }

    public Game(UUID gameId, List<Event> eventStream) {
        super(gameId, eventStream);
    }


    public void playNumber(final UUID playerId, Addition addition) {

        if (this.isFinished) {
            throw new GameFinishedException(getId().toString());
        }
        if ((playerId.equals(this.playerId) && !isPlayerOnMove) ||
                (!playerId.equals(this.playerId) && isPlayerOnMove)) {
            throw new WrongPlayerOnMoveException(getId().toString(), playerId.toString());
        }
        long newNumber = getNewNumber(addition);
        NumberAdditionEvent numberAdditionEvent = new NumberAdditionEvent(
                getId(), playerId, addition, newNumber, isGameFinished(newNumber), !playerId.equals(this.playerId));
        applyNewEvent(numberAdditionEvent);
    }

    @SuppressWarnings("unused")
    private void apply(GameStartEvent event) {

        this.playerId = event.getPlayerId();
        this.currentNumber = event.getCurrentNumber();
        this.isFinished = false;
        this.lastAddition = Addition.ZERO;
        this.isPlayerOnMove = false;
    }

    @SuppressWarnings("unused")
    private void apply(NumberAdditionEvent event) {

        this.isPlayerOnMove = event.isPlayerOnMove();
        this.lastAddition = Addition.get(event.getAddition());
        this.currentNumber = getNewNumber(lastAddition);
        this.isFinished = isGameFinished(event.getCurrentNumber());

    }

    private long getNewNumber(Addition addition) {

        long newNumber = this.currentNumber;
        if ((this.currentNumber + addition.getAddedNumber()) % 3 == 0) {
            newNumber = (this.currentNumber + addition.getAddedNumber()) / 3;
        }
        return newNumber;
    }

    private boolean isGameFinished(long currentNumber) {

        return currentNumber == 1;
    }

    public UUID getPlayerId() {

        return playerId;
    }

    public long getCurrentNumber() {

        return currentNumber;
    }

    public boolean isFinished() {

        return isFinished;
    }

    public boolean isPlayerOnMove() {

        return isPlayerOnMove;
    }

    public Addition getLastAddition() {

        return lastAddition;
    }
}
