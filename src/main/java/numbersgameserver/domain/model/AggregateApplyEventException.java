package numbersgameserver.domain.model;

public class AggregateApplyEventException extends RuntimeException {

    public AggregateApplyEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public AggregateApplyEventException(Throwable cause) {
        super(cause);
    }
}
