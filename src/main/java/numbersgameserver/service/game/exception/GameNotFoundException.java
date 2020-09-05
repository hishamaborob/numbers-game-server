package numbersgameserver.service.game.exception;

import java.util.UUID;

import static java.lang.String.format;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(UUID gameId) {

        super(format("Game with id '%s' could not be found", gameId));
    }
}
