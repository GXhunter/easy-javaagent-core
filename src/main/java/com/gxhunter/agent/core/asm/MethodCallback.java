package com.gxhunter.agent.core.asm;

import org.objectweb.asm.MethodVisitor;
@FunctionalInterface
public interface MethodCallback {
    MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions,
            MethodVisitor methodVisitor

    );
}
