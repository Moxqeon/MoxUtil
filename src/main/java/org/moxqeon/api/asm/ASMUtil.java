package org.moxqeon.api.asm;

import org.jetbrains.annotations.NotNull;
import org.moxqeon.api.context.Context;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

/**
 * ASM 字节码工具类，提供 getter 方法生成与动态类定义能力。
 */
public final class ASMUtil {
    private ASMUtil() {
        throw new IllegalStateException();
    }

    private static final ASMClassLoader LOADER = new ASMClassLoader();



    public byte[] generateGetter(byte[] classBytes, @NotNull String className, @NotNull String fieldName, @NotNull String methodName, @NotNull Class<?> fieldType) {
        String asmClassName = className.replace('.', '/');
        try {
            ClassReader cr = new ClassReader(classBytes);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cw.visit(V11, ACC_PUBLIC, asmClassName, null, "java/lang/Object", new String[]{"org/moxqeon/api/DataContainer"});
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, Type.getMethodDescriptor(Type.getType(fieldType)), null, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, asmClassName, fieldName, Type.getDescriptor(fieldType));
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
            cw.visitEnd();
            cr.accept(cw, 0);
            byte[] bytes = cw.toByteArray();
            Files.write(Path.of("E:\\Test1.class"), bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> define(@NotNull String className, byte[] bytes) {
        return LOADER.define(className, bytes);
    }

    public static Class<? extends Context> defineContext(@NotNull String className, byte[] bytes) {
        return LOADER.define(className, bytes).asSubclass(Context.class);
    }
}