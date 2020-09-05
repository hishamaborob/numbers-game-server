package numbersgameserver.domain.model.game.event;

import numbersgameserver.domain.model.Event;
import numbersgameserver.domain.model.game.Addition;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class NumberAdditionEvent extends Event {

    private final String playerId;
    private final int addition;
    private final long currentNumber;
    private final boolean isGameFinished;
    private final boolean isPlayerOnMove;

    public NumberAdditionEvent(
            UUID aggregateId, UUID playerId, Addition addition,
            long currentNumber, boolean isGameFinished, boolean isPlayerOnMove) {

        super(aggregateId);
        this.playerId = checkNotNull(playerId).toString();
        this.addition = checkNotNull(addition).getAddedNumber();
        this.currentNumber = currentNumber;
        this.isGameFinished = isGameFinished;
        this.isPlayerOnMove = isPlayerOnMove;
    }

    public UUID getPlayerId() {

        return UUID.fromString(playerId);
    }

    public int getAddition() {

        return addition;
    }

    public long getCurrentNumber() {

        return currentNumber;
    }

    public boolean isGameFinished() {

        return isGameFinished;
    }

    public boolean isPlayerOnMove() {

        return isPlayerOnMove;
    }
}
