package org.moxqeon.slf4j.fix;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Logback 消息转换器，将日志级别转换为对应首字母。
 */
public final class LogFix extends MessageConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return event.getLevel().levelStr.substring(0, 1);
    }
}