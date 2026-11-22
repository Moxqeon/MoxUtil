package org.moxqeon.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.MoxUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 日期格式化工具对象，提供常用日期格式与日期解析能力。
 */
public final class DateFormatUtil {
    private DateFormatUtil() {
        throw new IllegalStateException();
    }

    private static final List<SimpleDateFormat> customDateFormats = new ArrayList<>();

    static {
        MoxUtil.info("日期工具初始化成功");
        DateNTime.values();
        Date.values();
        Time.values();
    }

    @NotNull
    public static DateFormat buildFormat(@NotNull String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        customDateFormats.add(dateFormat);
        return dateFormat;
    }

    public static final class DateNTime {
        private DateNTime() {
            throw new IllegalStateException();
        }

        public static final DateFormat COMMON_1 = buildFormat("yyyy/MM/dd hh:mm:ss");
        public static final DateFormat COMMON_2 = buildFormat("yyyy/MM/dd  hh:mm:ss");
        public static final DateFormat HYPHEN_1 = buildFormat("yyyy-MM-dd hh:mm:ss");
        public static final DateFormat HYPHEN_2 = buildFormat("yyyy-MM-dd  hh:mm:ss");
        public static final DateFormat NONE = buildFormat("yyyyMMddHHmmss");

        public static DateFormat[] values() {
            return new DateFormat[]{COMMON_1, COMMON_2, HYPHEN_1, HYPHEN_2};
        }
    }

    public static final class Date {
        private Date() {
            throw new IllegalStateException();
        }

        public static final DateFormat SLASH = buildFormat("yyyy/MM/dd");
        public static final DateFormat HYPHEN = buildFormat("yyyy-MM-dd");

        public static DateFormat[] values() {
            return new DateFormat[]{SLASH, HYPHEN};
        }
    }

    public static final class Time {
        private Time() {
            throw new IllegalStateException();
        }

        public static final DateFormat COLON = buildFormat("hh:mm:ss");
        public static final DateFormat HYPHEN = buildFormat("hh-mm-ss");
        public static final DateFormat NONE = buildFormat("hhmmss");

        public static DateFormat[] values() {
            return new DateFormat[]{COLON, HYPHEN, NONE};
        }
    }

    @Nullable
    public static java.util.Date parse(@NotNull String dateString) {
        for (DateFormat dateFormat : customDateFormats)
            try {
                java.util.Date result = dateFormat.parse(dateString);
                if (dateFormat.format(result).equals(dateString))
                    return result;
            } catch (Exception ignored) {
            }
        return null;
    }
}