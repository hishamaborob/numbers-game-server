package numbersgameserver.projection;

import numbersgameserver.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMessageProducerAsync implements ClientMessageProducer {

    private final Map<UUID, Listener> clientsListeners = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void addClientListener(UUID clientId, Listener listener) {

        clientsListeners.put(clientId, listener);
    }

    @Override
    public void removeClientListener(UUID clientId) {

        try {
            clientsListeners.remove(clientId);
        } catch (NullPointerException e) {
            // ignore
        }
    }

    @Override
    public void sendMessage(UUID clientId, String message) {

        if (clientsListeners.containsKey(clientId)) {
            executorService.submit(() -> clientsListeners.get(clientId).onMessage(message));
        }
    }

    @Override
    public boolean isClientSubscribingToMessages(UUID clientId) {

        return clientsListeners.containsKey(clientId);
    }
}
