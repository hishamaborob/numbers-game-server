package numbersgameserver.adapter.engine;

import com.google.common.eventbus.Subscribe;
import numbersgameserver.domain.model.game.Addition;
import numbersgameserver.domain.model.game.event.GameStartEvent;
import numbersgameserver.domain.model.game.event.NumberAdditionEvent;
import numbersgameserver.service.game.GameService;
import numbersgameserver.service.game.command.NumberAdditionCommand;

import java.util.Random;
import java.util.UUID;

/**
 * Stupid player that listens to last moves events and reacts with random moves with the predefined range -1,0,1
 */
public class RandomGamePlayer {

    private static final UUID autoPlayerId = UUID.randomUUID();

    private GameService gameService;

    public RandomGamePlayer(GameService gameService) {
        this.gameService = gameService;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(GameStartEvent event) {

        gameMove(event.getAggregateId());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(NumberAdditionEvent event) {

        if (!event.isGameFinished() && !autoPlayerId.equals(event.getPlayerId())) {
            gameMove(event.getAggregateId());
        }
    }

    private void gameMove(UUID gameId) {

        gameService.process(new NumberAdditionCommand(gameId, autoPlayerId, Addition.get(rand())));
    }

    private static int rand() {

        int min = -1;
        int max = 1;
        return new Random().nextInt(max - min + 1) + min;
    }
}
