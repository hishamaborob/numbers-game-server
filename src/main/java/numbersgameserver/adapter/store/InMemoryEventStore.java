package numbersgameserver.adapter.store;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import numbersgameserver.domain.model.Event;
import numbersgameserver.domain.model.EventStore;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Store series of events that can be aggregated to build up the state.
 */
public class InMemoryEventStore implements EventStore {

    private final Map<UUID, List<Event>> eventStore = new ConcurrentHashMap<>();

    @Override
    public void store(UUID aggregateId, List<Event> newEvents) {
        eventStore.merge(aggregateId, newEvents, (oldValue, value) ->
                Stream.concat(oldValue.stream(), value.stream()).collect(toList()));
    }

    @Override
    public List<Event> load(UUID aggregateId) {
        return ImmutableList.copyOf(eventStore.getOrDefault(aggregateId, emptyList()));
    }
}
