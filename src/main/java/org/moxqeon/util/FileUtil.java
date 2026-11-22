package org.moxqeon.util;

import com.google.common.base.Preconditions;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.api.IOResult;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件与目录操作工具类，涵盖读写、复制、下载、哈希、清理及文件选择等功能。
 */
@Extension
public final class FileUtil {
    public static final String SLASH = System.getProperty("file.separator");

    public static final File USER_DIR = new File(System.getProperty("user.dir"));

    private FileUtil() {
        throw new IllegalStateException();
    }

    @NotNull
    public static File subFile(@NotNull File file, @NotNull String fileName) {
        Preconditions.checkArgument(file.exists() == file.isDirectory());
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException();
        return new File(file, fileName);
    }

    @NotNull
    public static List<File> existingFiles(@NotNull File file) {
        Preconditions.checkArgument(file.exists() == file.isDirectory());
        List<File> existingFiles = new ArrayList<>();
        if (!file.exists()) {
            if (file.mkdirs())
                return existingFiles;
            else
                throw new RuntimeException();
        }
        File[] files = file.listFiles();
        if (files != null)
            existingFiles.addAll(List.of(files));
        return existingFiles;
    }

    @NotNull
    public static List<File> existingFiles(@NotNull File file, @NotNull String ext) {
        Preconditions.checkArgument(file.exists() == file.isDirectory());
        List<File> existingFiles = new ArrayList<>();
        if (!file.exists()) {
            if (file.mkdirs())
                return existingFiles;
            else
                throw new RuntimeException();
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File subFile : files)
                if (subFile.getName().endsWith(ext))
                    existingFiles.add(subFile);
        }
        return existingFiles;
    }

    @NotNull
    public static List<File> recursiveFiles(@NotNull File file) {
        Preconditions.checkArgument(file.exists() == file.isDirectory());
        List<File> recursiveFiles = new ArrayList<>();
        if (!file.exists()) {
            if (file.mkdirs())
                return recursiveFiles;
            else
                throw new RuntimeException();
        }
        List<File> subFiles = existingFiles(file);
        for (File subFile : subFiles)
            if (subFile.isDirectory())
                recursiveFiles.addAll(recursiveFiles(subFile));
            else
                recursiveFiles.add(subFile);
        return recursiveFiles;
    }

    @NotNull
    public static String ext(@NotNull File file) {
        Preconditions.checkArgument(file.exists() == file.isFile());
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @NotNull
    public static String content(@NotNull File file) {
        Preconditions.checkArgument(file.exists() && file.isFile(), "File %s doesn't exist", file);
        try {
            return Files.readString(file.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String content(@NotNull File file, @NotNull Charset charset) {
        Preconditions.checkArgument(file.exists() && file.isFile(), "File %s doesn't exist", file);
        try {
            return Files.readString(file.toPath(), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static IOResult write(@NotNull File file, @NotNull String content) {
        return write(file, content, Charset.defaultCharset());
    }

    @NotNull
    public static IOResult write(@NotNull File file, @NotNull String content, @NotNull Charset charset) {
        File parent = file.getParentFile();
        if (createDir(parent).success() || parent.exists()) {
            try {
                Files.writeString(file.toPath(), content, charset);
                return IOResult.SUCCESS;
            } catch (IOException e) {
                return new IOResult(false, e);
            }
        }
        return IOResult.FAILURE_WITH_NO_EX;
    }

    @NotNull
    public static IOResult writeResource(@NotNull File file, @NotNull ClassLoader loader, @NotNull String resourcePath, @NotNull Charset charset) {
        try (InputStream input = loader.getResourceAsStream(resourcePath)) {
            if (input == null)
                return new IOResult(false, new IOException("Resource \"" + resourcePath + "\" not found"));
            return write(file, new String(input.readAllBytes(), charset), charset);
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    @NotNull
    public static IOResult copy(@NotNull File file, @NotNull File destination) {
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            try (OutputStream output = new BufferedOutputStream(new FileOutputStream(destination))) {
                output.write(input.readAllBytes());
                output.flush();
                return IOResult.SUCCESS;
            }
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    public static void clearOut(@NotNull File file) {
        Preconditions.checkArgument(file.exists() && file.isDirectory(), "Directory %s doesn't exist", file);
        for (File subFile : Preconditions.checkNotNull(file.listFiles())) {
            completelyDelete(subFile);
        }
    }

    public static void completelyDelete(@NotNull File file) {
        Preconditions.checkArgument(file.exists());
        if (file.isDirectory())
            for (File subFile : Preconditions.checkNotNull(file.listFiles())) {
                completelyDelete(subFile);
            }
        file.delete();
    }

    @NotNull
    public static IOResult createDir(@NotNull File file) {
        return new IOResult(file.mkdirs());
    }

    @NotNull
    public static IOResult create(@NotNull File file) {
        File parent = file.getParentFile();
        if (createDir(parent).success() || parent.exists()) {
            try {
                return new IOResult(file.createNewFile());
            } catch (IOException e) {
                return new IOResult(false, e);
            }
        }
        return IOResult.FAILURE_WITH_NO_EX;
    }

    @NotNull
    public static IOResult downloadFromURL(@NotNull File file, @NotNull URL source, @NotNull Charset charset) {
        try {
            String content = new String(source.openConnection().getInputStream().readAllBytes());
            return write(file, content, charset);
        } catch (IOException e) {
            return new IOResult(false, e);
        }
    }

    @NotNull
    public static IOResult downloadFromURL(@NotNull File file, @NotNull URL source) {
        return downloadFromURL(file, source, Charset.defaultCharset());
    }

    @NotNull
    public static String hash(@NotNull File file, @NotNull String algorithm) {
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            switch (algorithm.toUpperCase()) {
                case "MD2":
                    return DigestUtils.md2Hex(input);
                case "MD5":
                    return DigestUtils.md5Hex(input);
                case "SHA-1":
                    return DigestUtils.sha1Hex(input);
                case "SHA-256":
                    return DigestUtils.sha256Hex(input);
                case "SHA-512":
                    return DigestUtils.sha512Hex(input);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File selectSingleFile(@Nullable File dir, @NotNull String title, @NotNull FileFilter fileFilter) {
        dir = dir != null ? dir : USER_DIR;
        createDir(dir);
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setFileHidingEnabled(true);
        chooser.setFileFilter(fileFilter);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(chooser);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile();
    }
}