package org.moxqeon;

import java.util.logging.Logger;

/**
 * 日志门面类，统一 info/warn 日志输出，支持多种日志实现。
 */
public final class MoxUtil {
    private MoxUtil() {
        throw new IllegalStateException();
    }

    public static boolean debug = false;
    private static Logger logger = null;
    private static org.slf4j.Logger slf4jLogger = null;
    private static final System.Logger systemLogger = System.getLogger(MoxUtil.class.getName());

    public static void setLogger(Logger another) {
        logger = another;
    }

    public static void setLogger(org.slf4j.Logger another) {
        slf4jLogger = another;
    }

    public static void info(String info) {
        if (slf4jLogger != null) slf4jLogger.info(info);
        else if (logger != null) logger.info(info);
        else systemLogger.log(System.Logger.Level.INFO, info);
    }

    public static void info(String info, Object... args) {
        info(String.format(info, args));
    }

    public static void info(Object info) {
        info(info.toString());
    }

    public static void warn(String warn) {
        if (slf4jLogger != null) slf4jLogger.warn(warn);
        else if (logger != null) logger.warning(warn);
        else systemLogger.log(System.Logger.Level.WARNING, warn);
    }

    public static void warn(String warn, Object... args) {
        warn(String.format(warn, args));
    }

    public static void warn(Object warn) {
        warn(warn.toString());
    }
}