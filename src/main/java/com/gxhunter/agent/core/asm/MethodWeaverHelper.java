package com.gxhunter.agent.core.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方法织入辅助
 *
 * @author wanggx
 */
public class MethodWeaverHelper {

    public static final MethodWeaverHelper IGNORED_METHOD_SIGN = new MethodWeaverHelper();

    private static final Map<String, MethodWeaverHelper> plugin = new ConcurrentHashMap<>();

    public static Map<String, MethodWeaverHelper> getPlugin() {
        return plugin;
    }

    /**
     * 方法名 ---> 参数签名  ---> 方法消费者
     */
    private final Map<String, Map<String, MethodCallback>> methodVisitorCache = new ConcurrentHashMap<>();

    private static final Map<String, MethodCallback> empty = new HashMap<>();

    public static void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ClassWeaver.class)) {
            throw new IllegalStateException(clazz.getSimpleName() + "插件没有" + ClassWeaver.class.getSimpleName() + "注解");
        }
        String targetClass = clazz.getAnnotation(ClassWeaver.class).value();
        MethodWeaverHelper methodWeaverHelper = wovenClass(targetClass);
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(MethodWeaver.class)) {
                continue;
            }
            if (method.getReturnType() != MethodAdvice.class) {
                throw new IllegalStateException(method.getName() + "方法返回值只能是" + MethodAdvice.class.getSimpleName());
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalStateException(method.getName() + "必须是无参方法" + MethodVisitor.class.getSimpleName());
            }
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalStateException(method.getName() + "不是静态方法");
            }
            methodWeaverHelper.wovenMethod(
                    method.getAnnotation(MethodWeaver.class).methodName(),
                    method.getAnnotation(MethodWeaver.class).methodSign(),
                    (access, methodName, descriptor, signature, exceptions, methodVisitor) ->
                            new MethodVisitor(Opcodes.ASM9, null) {
                                @Override
                                public void visitCode() {
                                    try {
                                        System.out.println("method enhance:" + methodName + "@" + targetClass);
                                        ((MethodAdvice) method.invoke(clazz)).visitCode(methodVisitor);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
            );
        }
    }


    /**
     * 织入方法
     *
     * @param methodName    方法名
     * @param parameterSign 签名
     * @param consumer      回调
     * @return 自身
     */
    private MethodWeaverHelper wovenMethod(String methodName, String parameterSign, MethodCallback consumer) {
        methodVisitorCache.putIfAbsent(methodName, new ConcurrentHashMap<>());
        methodVisitorCache.get(methodName).put(parameterSign, consumer);
        return this;
    }

    public MethodCallback getMethodVisitor(String methodName, String parameterSign) {
        return methodVisitorCache.getOrDefault(methodName, empty).getOrDefault(parameterSign, (access, name, descriptor, signature, exceptions, methodVisitor) -> methodVisitor);
    }


    /**
     * 织入类
     *
     * @param className
     * @return
     */
    private static MethodWeaverHelper wovenClass(String className) {
        return plugin.computeIfAbsent(className, k -> new MethodWeaverHelper());
    }
}
