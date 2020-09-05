package numbersgameserver.domain.model;

import java.util.List;
import java.util.UUID;

public interface EventStore {

    void store(UUID aggregateId, List<Event> newEvents);

    List<Event> load(UUID aggregateId);

}