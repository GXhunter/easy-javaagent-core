package com.gxhunter.agent.core.asm;


import org.objectweb.asm.MethodVisitor;
@FunctionalInterface
public interface MethodAdvice  {
    /**
     * 重写方法
     * @param methodVisitor 原始方法
     */
    void visitCode(MethodVisitor methodVisitor);
}
