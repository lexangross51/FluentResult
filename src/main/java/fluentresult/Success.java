package fluentresult;

import fluentresult.abstractions.Reason;

public class Success implements Reason {
    private final String message;

    public Success(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}