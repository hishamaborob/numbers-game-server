package numbersgameserver.service.game.command;

import numbersgameserver.domain.model.game.Addition;

import java.util.UUID;

public class NumberAdditionCommand {

    private final UUID gameId;
    private final UUID playerId;
    private final Addition addition;

    public NumberAdditionCommand(UUID gameId, UUID playerId, Addition addition) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.addition = addition;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Addition getAddition() {
        return addition;
    }
}
