package fluentresult;

import fluentresult.abstractions.Reason;

public class Error implements Reason {
    protected final String message;

    public Error(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}