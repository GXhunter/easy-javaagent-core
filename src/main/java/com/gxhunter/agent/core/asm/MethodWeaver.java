package com.gxhunter.agent.core.asm;

import java.lang.annotation.*;

/**
 * @author wanggx
 * 必须加载静态方法上
 * 配合 {@link ClassWeaver} 使用
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodWeaver {
    /**
     * 方法名
     * @return 拦截的方法名
     */
    String methodName();

    /**
     * 方法签名
     * @return 签名详细
     */
    String methodSign();
}
