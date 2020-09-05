package numbersgameserver.service.game.command;

import java.util.UUID;

public class StartGameCommand {

    private final UUID challengerId;
    private final long currentNumber;

    public StartGameCommand(UUID challengerId, long currentNumber) {

        this.challengerId = challengerId;
        this.currentNumber = currentNumber;
    }

    public UUID getChallengerId() {
        return challengerId;
    }

    public long getCurrentNumber() {
        return currentNumber;
    }
}
