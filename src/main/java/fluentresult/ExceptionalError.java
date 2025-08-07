package fluentresult;

public class ExceptionalError extends Error {
    private final Exception exception;

    public ExceptionalError(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public ExceptionalError(Exception exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}