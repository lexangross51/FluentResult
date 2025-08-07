package fluentresult;

import java.util.List;

public class ResultValue<T> extends Result {
    private T value;

    public T getValue() {
        if (isFailed()) {
            throw new IllegalStateException("Cannot access value when result failed.");
        }

        return this.value;
    }

    public T getValueOrDefault() {
        return this.value;
    }

    public ResultValue<T> withValue(T value) {
        this.value = value;
        return this;
    }

    public static <T> ResultValue<T> ok(T value) {
        var result = new ResultValue<T>().withValue(value);
        result.withSuccesses(List.of(new Success("FluentResult.Success")));

        return result;
    }
}