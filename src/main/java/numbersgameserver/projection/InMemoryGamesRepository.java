package numbersgameserver.projection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory store with multiple data structure to manage clients queue, games, and messages.
 */
public class InMemoryGamesRepository implements GamesRepository {

    private final Map<UUID, GameProjection> games = new ConcurrentHashMap<>();
    private final Map<UUID, GameProjection> clientsGame = new ConcurrentHashMap<>();

    @Override
    public void addGame(UUID gameId, GameProjection gameProjection) {

        games.put(gameId, gameProjection);
    }

    @Override
    public void removeGame(UUID gameId) {

        games.remove(gameId);
    }

    @Override
    public GameProjection getGame(UUID gameId) {

        return games.get(gameId);
    }

    @Override
    public boolean isClientAddedToGame(UUID clientId) {

        return clientsGame.containsKey(clientId);
    }

    @Override
    public void addClientGame(UUID clientId, GameProjection gameProjection) {

        clientsGame.put(clientId, gameProjection);
    }

    @Override
    public GameProjection getClientGame(UUID clientId) {

        return clientsGame.get(clientId);
    }

    @Override
    public void removeClientGame(UUID clientId) {

        clientsGame.remove(clientId);
    }
}
