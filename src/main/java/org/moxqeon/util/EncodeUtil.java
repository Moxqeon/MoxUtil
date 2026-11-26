package org.moxqeon.util;

import manifold.ext.rt.api.This;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.CRC32;

/**
 * 编码与摘要工具类，提供 Base64、十六进制转换以及 MD5、SHA、CRC32 等摘要能力。
 */
public final class EncodeUtil {
    private EncodeUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static String encodeBase64(@NotNull String string) {
        return new String(Base64.getEncoder().encode(string.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @NotNull
    public static String decodeBase64(@NotNull String string) {
        return new String(Base64.getDecoder().decode(string.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @NotNull
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static byte[] hexToBytes(@NotNull String hex) {
        if (hex.length() % 2 != 0)
            hex = "0" + hex;
        int length = hex.length();
        byte[] result = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + (Character.digit(hex.charAt(i + 1), 16)));
        }
        return result;
    }

    @NotNull
    public static String md5Hex(@NotNull String sth) {
        return bytesToHex(DigestUtils.md5(sth));
    }

    @NotNull
    public static String sha1Hex(@NotNull String sth) {
        return bytesToHex(DigestUtils.sha1(sth));
    }

    @NotNull
    public static String sha256Hex(@NotNull String sth) {
        return bytesToHex(DigestUtils.sha256(sth));
    }

    @NotNull
    public static String crc32Hex(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return Long.toHexString(crc32.getValue());
    }

    public static boolean checkCRC32(byte[] check, @NotNull String hex) {
        return bytesToHex(check).equals(hex);
    }

    public static byte[] longToByteHex(long value) {
        return hexToBytes(Long.toHexString(value));
    }
}