package com.gxhunter.agent.core.plugin;

import java.lang.instrument.Instrumentation;
import java.util.List;

public interface PluginEntry {
    void init(Instrumentation inst);

    /**
     * 织入器
     * @return 织入者
     */
    List<Class<?>> weavers();
}
