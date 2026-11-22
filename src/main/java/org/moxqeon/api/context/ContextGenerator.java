package org.moxqeon.api.context;

import org.jetbrains.annotations.NotNull;
import org.moxqeon.api.asm.ASMGenerator;
import org.moxqeon.api.asm.ASMUtil;
import org.moxqeon.util.PrimitiveUtil;
import org.objectweb.asm.*;

import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * 基于 ASM 字节码动态生成 {@link Context} 实现类的生成器，生成的类以字段名为键提供取值能力。
 */
public final class ContextGenerator extends ASMGenerator<Context> {

    private final String className;
    private final String asmClassName;
    private final ClassWriter cw;
    private final LinkedHashMap<String, Class<?>> fields = new LinkedHashMap<>();
    private final List<String> fieldIndex = new ArrayList<>();
    private final Map<Integer, Label> labelMap = new HashMap<>();
    private final Map<String, Label> switchBranches = new HashMap<>();


    public ContextGenerator(@NotNull String className, @NotNull Class<?>... apis) {
        this.className = className;
        asmClassName = className.replace('.', '/');
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        String[] apiStrings = new String[apis.length + 1];
        for (int i = 0; i < apis.length; i++)
            apiStrings[i] = apis[i].getName().replace('.', '/');
        apiStrings[apis.length] = Context.class.getName().replace('.', '/');
        cw.visit(V11, ACC_PUBLIC, asmClassName, null, "java/lang/Object", apiStrings);

    }

    public void addField(@NotNull String fieldName, @NotNull Class<?> fieldType) {
        FieldVisitor fv = cw.visitField(ACC_PRIVATE, "field" + fields.size(), Type.getDescriptor(fieldType), null, null);
        fv.visitEnd();
        fields.put(fieldName, fieldType);
        if (!labelMap.containsKey(fieldName.hashCode())) labelMap.put(fieldName.hashCode(), new Label());
        if (!switchBranches.containsKey(fieldName)) switchBranches.put(fieldName, new Label());
        if (!fieldIndex.contains(fieldName)) fieldIndex.add(fieldName);
    }

    public ContextGenerator generateGeneralGetter() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(String.class)), null, null);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "hashCode", Type.getMethodDescriptor(Type.INT_TYPE), false);
        Label defaultLabel = new Label();
        Label endLabel = new Label();
        mv.visitLookupSwitchInsn(defaultLabel, PrimitiveUtil.copy(labelMap.keySet().toArray(new Integer[0])), labelMap.values().toArray(new Label[0]));
        for (Map.Entry<Integer, Label> entry : labelMap.entrySet()) {
            mv.visitLabel(labelMap.get(entry.getKey()));
            for (Map.Entry<String, Class<?>> fieldEntry : fields.entrySet()) {
                String fieldName = fieldEntry.getKey();
                Class<?> fieldType = fieldEntry.getValue();
                if (fieldName.hashCode() == entry.getKey()) {
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(fieldName);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(Object.class)), false);
                    Label next = switchBranches.get(fieldName);
                    mv.visitJumpInsn(IFEQ, next);

                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, asmClassName, "field" + fieldIndex.indexOf(fieldName), Type.getDescriptor(fieldType));

                    mv.visitInsn(ARETURN);
                    mv.visitLabel(next);
                }
            }
        }
        mv.visitLabel(defaultLabel);
        mv.visitJumpInsn(GOTO, endLabel);
        mv.visitLabel(endLabel);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        return this;
    }

    public ContextGenerator generateConstructor() {
        Type[] constructorParams = new Type[fields.size()];
        int i = 0;
        for (Class<?> fieldType : fields.values()) {
            constructorParams[i] = Type.getType(fieldType);
            i++;
        }
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, constructorParams), null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", Type.getMethodDescriptor(Type.VOID_TYPE), false);
        i = 0;
        for (Map.Entry<String, Class<?>> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Class<?> fieldType = entry.getValue();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, i + 1);
            mv.visitFieldInsn(PUTFIELD, asmClassName, "field" + fieldIndex.indexOf(fieldName), Type.getDescriptor(fieldType));
            i++;
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        return this;
    }

    @NotNull
    public Class<? extends Context> generate() {
        cw.visitEnd();
        byte[] data = cw.toByteArray();
        return ASMUtil.defineContext(className, data);
    }
}