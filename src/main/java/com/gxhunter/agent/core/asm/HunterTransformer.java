package com.gxhunter.agent.core.asm;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * class类文件转换器
 *
 * @see MethodWeaverHelper 插件
 */
public class HunterTransformer implements ClassFileTransformer {

    private final Map<String, MethodWeaverHelper> plugin;

    public HunterTransformer(Map<String, MethodWeaverHelper> plugin) {
        this.plugin = plugin;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        String targetFullClassName = className.replaceAll("/", "\\.");
        MethodWeaverHelper methodSign = plugin.getOrDefault(targetFullClassName, MethodWeaverHelper.IGNORED_METHOD_SIGN);
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(Opcodes.F_FULL);
        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = this.cv.visitMethod(access, methodName, descriptor, signature, exceptions);
                return methodSign.getMethodVisitor(methodName, descriptor).visitMethod(access, methodName, descriptor, signature, exceptions, mv);
            }
        }, Opcodes.F_FULL);
        return cw.toByteArray();
    }
}
