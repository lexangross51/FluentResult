package fluentresult;

import fluentresult.abstractions.Reason;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Result {
    protected final List<Reason> reasons = new ArrayList<>();

    public boolean isSuccess() {
        return reasons
                .stream()
                .noneMatch(r -> r instanceof Error);
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public List<Error> getErrors() {
        var errors = new ArrayList<Error>();

        for (var r : reasons) {
            if (r instanceof Error) {
                errors.add((Error)r);
            }
        }

        return errors;
    }

    public List<Success> getSuccesses() {
        var successes = new ArrayList<Success>();

        for (var r : reasons) {
            if (r instanceof Success) {
                successes.add((Success) r);
            }
        }

        return successes;
    }

    public Result withErrors(List<Error> errors) {
        this.reasons.addAll(errors);
        return this;
    }

    public Result withSuccesses(List<Success> successes) {
        this.reasons.addAll(successes);
        return this;
    }

    public Result mapErrors(Function<Error, Error> mapper) {
        if (isSuccess()) {
            return this;
        }

        var mapped = new ArrayList<Error>();

        for (var e : getErrors()) {
            mapped.add(mapper.apply(e));
        }

        return new Result()
                .withErrors(mapped)
                .withSuccesses(getSuccesses());
    }

    public Result mapSuccesses(Function<Success, Success> mapper) {
        var mapped = new ArrayList<Success>();

        for (var s : getSuccesses()) {
            mapped.add(mapper.apply(s));
        }

        return new Result()
                .withErrors(getErrors())
                .withSuccesses(mapped);
    }

    public <T> ResultValue<T> toResult(T value) {
        var result = new ResultValue<T>();

        if (!isFailed()) {
            var _ = result.withValue(value);
        }

        var _ = result.withReasons(this.reasons);
        return result;
    }

    public <T> ResultValue<T> bind(Function<Void, ResultValue<T>> bind) {
        var result = new ResultValue<T>();
        result.withReasons(this.reasons);

        if (isSuccess()) {
            ResultValue<T> bound = bind.apply(null);
            var _ = result.withValue(bound.getValueOrDefault());
            var _ = result.withReasons(bound.getReasons());
        }

        return result;
    }

    protected Result withReasons(List<Reason> reasons) {
        this.reasons.addAll(reasons);
        return this;
    }

    public List<Reason> getReasons() {
        return this.reasons;
    }

    public static Result ok() {
        return new Result().withSuccesses(List.of(new Success("FluentResult.Success")));
    }

    public static Result fail(String message) {
        return new Result().withErrors(List.of(new Error(message)));
    }

    public static Result fail(List<Error> errors) {
        return new Result().withErrors(errors);
    }

    public static Result fail(Exception exception) {
        return fail(List.of(new ExceptionalError(exception)));
    }
}