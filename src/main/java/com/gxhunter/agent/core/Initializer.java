package com.gxhunter.agent.core;

import com.gxhunter.agent.core.asm.HunterTransformer;
import com.gxhunter.agent.core.asm.MethodWeaverHelper;
import com.gxhunter.agent.core.plugin.PluginClassLoader;
import com.gxhunter.agent.core.plugin.PluginEntry;
import com.gxhunter.agent.core.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Initializer implements AgentConst.ManiFestAttrKey,AgentConst.LogPrinter {

    public static synchronized void init(final Instrumentation inst, File file, String pluginUri) {
//        插件管理器
        try {
            JarFile jarFile = new JarFile(file);
            Manifest manifest = jarFile.getManifest();
            String entryClass = manifest.getMainAttributes().getValue(ENTRY_NAME);
            if (StringUtils.isEmpty(entryClass)) {
                return;
            }
            long startTime = System.currentTimeMillis();
            ClassLoader classLoader = new PluginClassLoader(jarFile);
            Class<?> klass = Class.forName(entryClass, false, classLoader);
            inst.appendToBootstrapClassLoaderSearch(jarFile);
            System.out.printf(
                    "%s加载插件:\t[%s:%s] by %s\nfrom: %s \n ",
                    DIVIDER,
                    manifest.getMainAttributes().getValue(PLUGIN_NAME),
                    manifest.getMainAttributes().getValue(VERSION),
                    manifest.getMainAttributes().getValue(AUTHOR),
                    pluginUri
            );
            PluginEntry pluginEntry = (PluginEntry) Class.forName(entryClass).newInstance();
            pluginEntry.weavers().forEach(MethodWeaverHelper::register);
            HunterTransformer transformer = new HunterTransformer(MethodWeaverHelper.getPlugin());
            inst.addTransformer(transformer, true);
            inst.setNativeMethodPrefix(transformer, StringUtils.randomMethodName(15) + "_");
            for (Class loadedClass : inst.getAllLoadedClasses()) {
                if (MethodWeaverHelper.getPlugin().containsKey(loadedClass.getName())) {
                    System.out.println("hunter reload class：" + loadedClass.getName());
                    inst.retransformClasses(loadedClass);
                }
            }
            pluginEntry.init(inst);
            System.out.printf("\n插件(%s)加载完毕,耗时: %s %s",
                    manifest.getMainAttributes().getValue(PLUGIN_NAME),
                    (System.currentTimeMillis() - startTime),
                    DIVIDER
            );
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnmodifiableClassException e) {
            e.printStackTrace();
            System.exit(AgentConst.ExitCode.ERROR_LOADER_PLUGIN);
        }
    }

}
