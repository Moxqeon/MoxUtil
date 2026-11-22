package org.moxqeon.util;

import manifold.ext.rt.api.Extension;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 系统命令与线程工具类，提供系统命令执行、休眠与简单异步运行等能力。
 */
@Extension
public final class SystemUtil {
    private SystemUtil() {
        throw new IllegalStateException();
    }

    public static int runAsSystemCommand(@NotNull String command, boolean inheritIO) {
        String osName = System.getProperty("os.name");
        try {
            ProcessBuilder builder = osName.toLowerCase().startsWith("win")
                    ? new ProcessBuilder("cmd", "/c", command)
                    : new ProcessBuilder("sh", "-c", command);
            if (inheritIO)
                builder.inheritIO();
            return builder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runSimply(@NotNull Runnable runnable, boolean async) {
        if (async)
            new Thread(runnable).start();
        else
            runnable.run();
    }
}