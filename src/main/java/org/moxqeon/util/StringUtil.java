package org.moxqeon.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.gson.JsonObject;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.api.compound.Compound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类，涵盖正则查找、大小写转换、随机占位、哈希、进制转换等功能。
 */
@Extension
public final class StringUtil {
    private StringUtil() {
        throw new IllegalStateException();
    }

    private static final String HEX_CHARS = "0123456789abcdef";
    private static final char[] CHINESE_NUMBERS = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

    @NotNull
    public static List<String> find(@This @NotNull String $this, @NotNull String regex) {
        final List<String> result = new ArrayList<>();
        final Matcher matcher = Pattern.compile(regex).matcher($this);
        while (matcher.find())
            result.add(matcher.group());
        return result;
    }

    public static int count(@This @NotNull String $this, @NotNull String regex) {
        return ($this.split(regex)).length - 1;
    }

    @NotNull
    public static String replaceLast(@This @NotNull String $this, @NotNull String regex, @NotNull String replacement) {
        if ($this.endsWith(regex))
            return $this.substring(0, $this.length() - regex.length());
        int index = replacement.lastIndexOf(regex);
        if (index == -1)
            return $this;
        return $this.substring(0, index) + replacement + $this.substring(index);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @NotNull
    public static String replaceRandomInt(@This @NotNull String $this) {
        Random random = new Random();
        List<String> randomInts = find($this, "<random:-?\\d+~-?\\d+>");
        List<String> randomExpectedInts = find($this, "<random:-?\\d+~-?\\d+:\\d+>");
        for (String randomInt : randomInts) {
            final Compound compound = new Compound(randomInt.substring(8, randomInt.length() - 1), "~");
            final int min = Integer.parseInt(compound.get(0));
            final int max = Integer.parseInt(compound.get(1));
            $this = $this.replaceFirst(randomInt, String.valueOf(MathUtil.nextInt(random, min, max)));
        }
        for (String randomExpectedInt : randomExpectedInts) {
            final String[] temp = randomExpectedInt.substring(8, randomExpectedInt.length() - 1).split("~");
            final Compound compound = new Compound(temp[0], "~");
            final int min = Integer.parseInt(compound.get(0));
            final int max = Integer.parseInt(compound.get(1));
            final int expect = Integer.parseInt(temp[1]);
            $this = $this.replaceFirst(randomExpectedInt, String.valueOf(MathUtil.nextInt(random, min, max, expect)));
        }
        return $this;
    }

    public static boolean containsRandomInt(@This @NotNull String $this) {
        return ($this.contains("<random:-?\\d+~-?\\d+>") || $this.contains("<random:-?\\d+~-?\\d+:\\d+>"));
    }

    public static boolean isUpperCase(@This @NotNull String $this) {
        return $this.toUpperCase().equals($this);
    }

    public static boolean isLowerCase(@This @NotNull String $this) {
        return $this.toLowerCase().equals($this);
    }

    public static boolean isSnakeCase(@This @NotNull String $this) {
        return $this.matches("[a-z_0-9]+");
    }

    public static boolean isCamelCase(@This @NotNull String $this) {
        return $this.matches("[A-Za-z0-9]+");
    }

    @NotNull
    public static String camelToSnake(@This @NotNull String $this) {
        if (isSnakeCase($this))
            return $this;
        List<String> camelList = find($this, "[A-Z]");
        for (String camel : camelList)
            $this = $this.replaceFirst(camel, "_" + camel.toLowerCase());
        return ($this.charAt(0) == '_') ? $this.substring(1) : $this;
    }

    @NotNull
    public static String snakeToCamel(@This @NotNull String $this) {
        if (isCamelCase($this))
            return $this;
        List<String> snakeList = find($this, "_[a-z]");
        for (String snake : snakeList)
            $this = $this.replaceFirst(snake, snake.substring(1).toUpperCase());
        return $this;
    }

    public static boolean isPositiveLong(@This @NotNull String $this) {
        return $this.matches("\\+?\\d+");
    }

    public static boolean isLong(@This @NotNull String $this) {
        return $this.matches("[-|+]?\\d+");
    }

    public static boolean isPositiveDouble(@This @NotNull String $this) {
        return $this.matches("\\+?\\d+(\\.\\d+)?");
    }

    public static boolean isDouble(@This @NotNull String $this) {
        return $this.matches("[-|+]?\\d+(\\.\\d+)?");
    }

    public static int parseHex(@NotNull String $this) {
        Preconditions.checkArgument(($this.length() == 6), "Invalid hex length");
        Preconditions.checkArgument($this.matches("[0-9A-Fa-f]+"), "Invalid hex format");
        return Integer.parseInt($this, 16);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    public static JsonObject toJson(@This @NotNull String $this) {
        return JsonUtil.PARSER.parse($this).getAsJsonObject();
    }

    @NotNull
    public static Range<Integer> asIntRange(@This @NotNull String $this) {
        final String[] tmp = $this.split("~");
        Preconditions.checkArgument(tmp.length < 3);
        return Range.closed(Integer.parseInt(tmp[0]), Integer.parseInt(tmp.length == 2 ? tmp[1] : tmp[0]));
    }

    @NotNull
    public static Range<Double> asDoubleRange(@This @NotNull String $this) {
        final String[] tmp = $this.split("~");
        Preconditions.checkArgument(tmp.length < 3);
        return Range.closed(Double.parseDouble(tmp[0]), Double.parseDouble(tmp.length == 2 ? tmp[1] : tmp[0]));
    }

    public static String hash(@This @NotNull String $this, @NotNull String algorithm) {
        switch (algorithm.toUpperCase()) {
            case "MD2":
                return DigestUtils.md2Hex($this);
            case "MD5":
                return DigestUtils.md5Hex($this);
            case "SHA-1":
                return DigestUtils.sha1Hex($this);
            case "SHA-256":
                return DigestUtils.sha256Hex($this);
            case "SHA-512":
                return DigestUtils.sha512Hex($this);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static String randomHex(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(HEX_CHARS.charAt(random.nextInt(16)));
        }
        return stringBuilder.toString();
    }

    public static int alphabet2Int(@NotNull String alphabet) {
        int result = 0;
        char[] chars = alphabet.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int power = (int) Math.pow(26, chars.length - i - 1);
            result += (chars[i] - 'A' + 1) * power;
        }
        return result;
    }

    public static String toString(int i, int digitNumber) {
        StringBuilder str = new StringBuilder(String.valueOf(i));
        while (str.length() < digitNumber) {
            str.insert(0, "0");
        }
        return str.toString();
    }

    public static String toChineseUpper(long number) {
        return toChineseUpper((int) number);
    }

    public static String toChineseUpper(int number) {
        Preconditions.checkArgument(number >= 0);
        if (number == 0)
            return String.valueOf(CHINESE_NUMBERS[0]);
        else {
            StringBuilder result = new StringBuilder();
            while (number > 0) {
                int digit = number % 10;
                result.insert(0, CHINESE_NUMBERS[digit]);
                number /= 10;
            }
            return result.toString();
        }
    }

    public static boolean isChineseChar(char c) {
        return (c >= '\u4e00' && c <= '\u9fff') || (c >= '\u3400' && c <= '\u4dbf');
    }
}