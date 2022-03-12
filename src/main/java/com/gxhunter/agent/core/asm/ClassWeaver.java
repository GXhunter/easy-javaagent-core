package com.gxhunter.agent.core.asm;

import java.lang.annotation.*;

/**
 * @author wanggx
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassWeaver {
    /**
     * 全类名,对应 {@link Class#getName()}
     * @return 类全路径名称
     */
    String value();
}
