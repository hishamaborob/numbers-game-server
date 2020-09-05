package numbersgameserver.projection;

import numbersgameserver.Listener;

import java.util.UUID;

public interface ClientMessageProducer {

    void addClientListener(UUID clientId, Listener listener);

    void removeClientListener(UUID clientId);

    void sendMessage(UUID clientId, String message);

    boolean isClientSubscribingToMessages(UUID clientId);
}
