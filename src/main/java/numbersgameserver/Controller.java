package numbersgameserver;

import java.util.UUID;

public interface Controller {

    void registerListener(UUID clientId, Listener listener);

    void unregisterListener(UUID clientId);

    void postCommand(UUID clientId, String command);
}
