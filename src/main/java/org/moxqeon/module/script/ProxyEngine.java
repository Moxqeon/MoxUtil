package org.moxqeon.module.script;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.jetbrains.annotations.NotNull;
import org.moxqeon.MoxUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * GraalVM JavaScript 引擎封装，提供类/对象导入、脚本编译与变量取值等能力。
 *
 * @param <T> 用于标识上下文的键类型
 */
public abstract class ProxyEngine<T> {
    private final ClassLoader classLoader;
    private final Engine jsEngine;
    private final Map<String, Object> globalObjects = new HashMap<>();
    private final Map<T, Context> contexts = new HashMap<>();

    public ProxyEngine(@NotNull Class<?> clazz) {
        this.classLoader = clazz.getClassLoader();
        this.jsEngine = Engine.create("js");
        globalObjects.put("Tool", JavaScriptTool.class);
        globalObjects.put("engine", this);
    }

    public ProxyEngine() {
        this(MoxUtil.class);
    }

    /**
     * 创建一个可持续的上下文
     *
     * @param key 上下文的关联键
     * @return 新的上下文
     */
    public Context startContext(@NotNull T key) {
        Context context = Context.newBuilder("js").engine(jsEngine).build();
        for (Map.Entry<String, Object> entry : globalObjects.entrySet())
            context.getBindings("js").putMember(entry.getKey(), entry.getValue());
        contexts.put(key, context);
        return context;
    }

    /**
     * 创建临时的上下文
     *
     * @return 临时上下文
     */
    public Context createContext() {
        Context context = Context.newBuilder("js").engine(jsEngine).build();
        for (Map.Entry<String, Object> entry : globalObjects.entrySet())
            context.getBindings("js").putMember(entry.getKey(), entry.getValue());
        return context;
    }

    /**
     * 关闭上下文
     *
     * @param key   上下文的关联键
     * @param force 是否停止任务并强制关闭
     */
    public void stopContext(@NotNull T key, boolean force) {
        Context context = contexts.get(key);
        context.close(force);
        contexts.remove(key);
    }

    /**
     * 获取上下文
     *
     * @param key 上下文的关联键
     * @return 上下
     */
    @NotNull public Context get(@NotNull T key) {
        return contexts.get(key);
    }

}