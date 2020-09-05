package numbersgameserver.domain.model.game.exception;

import static java.lang.String.format;

public class WrongPlayerOnMoveException extends RuntimeException {

    public WrongPlayerOnMoveException(String gameId, String playerId) {

        super(format("Player %s has to wait for rival's move in game %s", playerId, gameId));
    }
}
