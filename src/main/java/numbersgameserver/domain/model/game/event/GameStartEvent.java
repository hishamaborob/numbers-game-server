package numbersgameserver.domain.model.game.event;

import numbersgameserver.domain.model.Event;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameStartEvent extends Event {

    private final String playerId;
    private final long currentNumber;

    public GameStartEvent(UUID aggregateId, UUID playerId, long currentNumber) {

        super(aggregateId);
        this.playerId = checkNotNull(playerId).toString();
        this.currentNumber = checkNotNull(currentNumber);
    }

    public UUID getPlayerId() {
        return UUID.fromString(playerId);
    }

    public long getCurrentNumber() {
        return currentNumber;
    }
}
