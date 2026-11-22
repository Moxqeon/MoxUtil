package org.moxqeon.api;

import java.io.IOException;

/**
 * IO 操作结果，封装成功标志与可能出现的异常。
 */
public class IOResult {
    public static final IOResult SUCCESS = new IOResult(true);
    public static final IOResult FAILURE_WITH_NO_EX = new IOResult(false);

    private final boolean success;
    private final IOException exception;

    public IOResult(boolean success) {
        this(success, null);
    }

    public IOResult(boolean success, IOException exception) {
        this.success = success;
        this.exception = exception;
    }

    public boolean success() {
        return success;
    }

    public IOException getException() {
        return exception;
    }
}