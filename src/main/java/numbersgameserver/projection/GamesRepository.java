package numbersgameserver.projection;

import java.util.UUID;

public interface GamesRepository {
    
    void addGame(UUID gameId, GameProjection gameProjection);
    
    void removeGame(UUID gameId);
    
    GameProjection getGame(UUID gameId);
    
    boolean isClientAddedToGame(UUID clientId);
    
    void addClientGame(UUID clientId, GameProjection gameProjection);
    
    GameProjection getClientGame(UUID clientId);
    
    void removeClientGame(UUID clientId);
}
