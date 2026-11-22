package org.moxqeon.module.zip;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 流式 ZIP 压缩容器，通过内部指针定位当前目录，支持逐文件或逐目录写入压缩包。
 */
public final class ZipContainer {
    public static final String SEPARATOR = "/";
    private final ZipOutputStream output;
    private String pointer = SEPARATOR;

    public ZipContainer(@NotNull File zipFile) {
        try {
            this.output = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPointer(@NotNull String pointer) {
        System.out.println("Current Pointer: " + pointer);
        this.pointer = pointer;
    }

    public String getPointer() {
        return pointer;
    }

    public void createFolder(@NotNull String folder) {
        try {
            String pointerName;
            if (folder.startsWith(SEPARATOR)) {
                pointerName = folder;
            } else {
                pointerName = pointer + folder;
            }
//            String dirName = pointerName.substring(1);
            output.putNextEntry(new ZipEntry(pointerName.substring(1)));
            output.closeEntry();
            setPointer(pointerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void compressFolder(@NotNull File folder) {
        Preconditions.checkArgument(folder.exists() && folder.isDirectory(), "Folder %s doesn't exist", folder);
        createFolder(folder.getName() + SEPARATOR);
        for (File subFile : FileUtil.existingFiles(folder)) {
            if (subFile.isDirectory()) compressFolder(subFile);
            else compressFile(subFile);
        }
        backToParent();
    }

    public void compressFile(@NotNull File file) {
        Preconditions.checkArgument(file.exists() && file.isFile(), "File %s doesn't exist", file);
        try {
            output.putNextEntry(new ZipEntry(pointer.substring(1) + file.getName()));
            byte[] data = Files.readAllBytes(file.toPath());
            output.write(data);
            output.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void compressContent(@NotNull String name, byte[] content) {
        try {
            output.putNextEntry(new ZipEntry(pointer.substring(1) + name));
            output.write(content);
            output.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToParent() {
        setPointer(pointer.substring(0, pointer.lastIndexOf(SEPARATOR, pointer.length() - 2) + 1));
    }
}