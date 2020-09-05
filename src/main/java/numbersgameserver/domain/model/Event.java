package numbersgameserver.domain.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

public abstract class Event {

    private final UUID aggregateId;

    public Event(UUID aggregateId) {
        this.aggregateId = checkNotNull(aggregateId);
    }

    public UUID getAggregateId() {
        return aggregateId;
    }
}
