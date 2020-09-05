package numbersgameserver.domain.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Collections.emptyList;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Aggregate {

    private UUID id;
    private List<Event> newEvents;

    protected Aggregate(UUID id) {
        this(id, emptyList());
    }

    protected Aggregate(UUID id, List<Event> eventStream) {
        checkNotNull(id);
        checkNotNull(eventStream);
        this.id = id;
        eventStream.forEach(this::apply);
        this.newEvents = new ArrayList<>();
    }

    protected void applyNewEvent(Event event) {
        apply(event);
        newEvents.add(event);
    }

    private void apply(Event event) {
        try {
            Method method = this.getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (InvocationTargetException e) {
            throw new AggregateApplyEventException("Failed to apply event on aggregate "
                    + event.getAggregateId(), e.getCause());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new UnsupportedOperationException(
                    format("Aggregate '%s' doesn't apply event type '%s'", this.getClass(), event.getClass()), e);
        }
    }

    public UUID getId() {
        return id;
    }

    public List<Event> getNewEvents() {
        return ImmutableList.copyOf(newEvents);
    }
}
