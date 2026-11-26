package org.moxqeon.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.api.IOResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZIP 压缩与解压工具类。
  @see org.moxqeon.module.zip.ZipContainer
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@Deprecated(forRemoval = true)
public final class ZipUtil {
    private ZipUtil() {
        throw new IllegalStateException();
    }

    public static IOResult compress(@NotNull File $this, @NotNull File... sources) {
        return compress($this, Arrays.asList(sources));
    }

    public static IOResult compress(@NotNull File $this, @NotNull List<File> sources) {
        try (ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(file))) {
            for (File source : sources)
                compressFileToZip(source, zipOutput, source.getName());
            return IOResult.SUCCESS;
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    public static IOResult decompressAll(@NotNull ZipFile $this, @NotNull File destDir) {
        destDir.mkdirs();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            try (InputStream in = $this.getInputStream(zipEntry)) {
                byte[] data = in.readAllBytes();
                File decompressed = new File(destDir, zipEntry.getName().replaceAll("/", Matcher.quoteReplacement(FileUtil.SLASH)));
                decompressed.getParentFile().mkdirs();
                Files.write(decompressed.toPath(), data);
            } catch (IOException e) {
                return new IOResult(false, e);
            }
        }
        return IOResult.SUCCESS;
    }

    public static IOResult decompress(@NotNull ZipFile $this, @NotNull String path, @NotNull File destDir) {
        destDir.mkdirs();
        ZipEntry zipEntry = new ZipEntry(path);
        try (InputStream in = $this.getInputStream(zipEntry)) {
            byte[] data = in.readAllBytes();
            File decompressed = new File(destDir, path.replaceAll("/", Matcher.quoteReplacement(FileUtil.SLASH)));
            decompressed.getParentFile().mkdirs();
            Files.write(decompressed.toPath(), data);
            return IOResult.SUCCESS;
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    public static IOResult decompress(@NotNull ZipFile $this, @NotNull ZipEntry zipEntry, @NotNull File destDir) {
        destDir.mkdirs();
        try (InputStream in = $this.getInputStream(zipEntry)) {
            byte[] data = in.readAllBytes();
            File decompressed = new File(destDir, zipEntry.getName().replaceAll("/", Matcher.quoteReplacement(FileUtil.SLASH)));
            decompressed.getParentFile().mkdirs();
            Files.write(decompressed.toPath(), data);
            return IOResult.SUCCESS;
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    public static byte[] content(@NotNull ZipFile $this, @NotNull String path) {
        return content($this, new ZipEntry(path));
    }

    public static byte[] content(@NotNull ZipFile $this, @NotNull ZipEntry zipEntry) {
        try (InputStream in = $this.getInputStream(zipEntry)) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void compressFileToZip(@NotNull File source, @NotNull ZipOutputStream zip, @NotNull String name) {
        Preconditions.checkArgument(source.exists(), "File %s doesn't exist", source);
        try {
            if (source.isFile()) {
                zip.putNextEntry(new ZipEntry(name));
                byte[] data = Files.readAllBytes(source.toPath());
                zip.write(data);
                zip.closeEntry();
            } else {
                File[] files = source.listFiles();
                if (files == null) {
                    zip.putNextEntry(new ZipEntry(name + "/"));
                    zip.closeEntry();
                } else {
                    for (File file : files)
                        compressFileToZip(file, zip, name + "/" + name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}