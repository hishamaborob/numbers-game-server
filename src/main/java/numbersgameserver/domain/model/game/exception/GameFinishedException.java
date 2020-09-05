package numbersgameserver.domain.model.game.exception;

import static java.lang.String.format;

public class GameFinishedException extends RuntimeException {

    public GameFinishedException(String gameId) {

        super(format("Game %s is finished already", gameId));
    }
}
