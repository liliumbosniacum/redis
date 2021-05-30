package com.lilium.redis.service.locker;

public class LockExecutionResult<T> {
    private final boolean lockAcquired;
    public final T resultIfLockAcquired;
    public final Exception exception;

    private LockExecutionResult(boolean lockAcquired, T resultIfLockAcquired, final Exception exception) {
        this.lockAcquired = lockAcquired;
        this.resultIfLockAcquired = resultIfLockAcquired;
        this.exception = exception;
    }

    public static <T> LockExecutionResult<T> buildLockAcquiredResult(final T result) {
        return new LockExecutionResult<>(true, result, null);
    }

    public static <T> LockExecutionResult<T> buildLockAcquiredWithException(final Exception e) {
        return new LockExecutionResult<>(true, null, e);
    }

    public static <T> LockExecutionResult<T> lockNotAcquired() {
        return new LockExecutionResult<>(false, null, null);
    }

    public boolean isLockAcquired() {
        return lockAcquired;
    }

    public T getResultIfLockAcquired() {
        return resultIfLockAcquired;
    }

    public boolean hasException() {
        return exception != null;
    }
}
