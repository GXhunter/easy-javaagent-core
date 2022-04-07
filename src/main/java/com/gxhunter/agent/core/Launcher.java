package com.gxhunter.agent.core;

import com.gxhunter.agent.core.utils.IOUtils;
import com.gxhunter.agent.core.utils.WhereIsUtils;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Launcher implements AgentConst.ExitCode, AgentConst.ManiFestAttrKey, AgentConst.LogPrinter {
    private static boolean loaded = false;

    public static void agentmain(String args, Instrumentation inst) {
        if (null == System.getProperty("hunter.debug")) {
            System.setProperty("hunter.debug", "1");
        }

        premain(args, inst);
    }


    /**
     * @param args
     * @param inst
     */
    public static void premain(String args, Instrumentation inst) {
        try {
            if (loaded) {
                return;
            }
            loaded = true;
            URI jarURI = WhereIsUtils.getJarURI();
            File agentFile = new File(jarURI);
            System.setProperty(AgentConst.SystemEnvKey.AGENT_MD5, IOUtils.md5(agentFile));
            System.setProperty(AgentConst.SystemEnvKey.AGENT_URI, jarURI.toString());
            JarFile jarFile = new JarFile(agentFile);
            printUsage(jarFile.getManifest());
            if (args == null || args.isEmpty()) {
                Initializer.init(inst,agentFile,jarURI.toString());
            } else {
                inst.appendToBootstrapClassLoaderSearch(jarFile);
                for (String pluginUri : args.split(",")) {
                    pluginUri = URLDecoder.decode(pluginUri, "UTF-8");
                    File pluginFile = File.createTempFile("plugin", ".jar");
                    try (FileOutputStream fos = new FileOutputStream(pluginFile);
                         InputStream in = IOUtils.download(pluginUri);
                    ) {
                        IOUtils.copy(in, fos);
                        Initializer.init(inst, pluginFile, pluginUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(EXIT_SSH_ERROR);
                    }
                }
            }
        } catch (URISyntaxException | IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void printUsage(Manifest manifest) {
        String message = new StringBuilder()
                .append(DIVIDER).append(NEWLINE)
                .append("\t\t").append(manifest.getMainAttributes().getValue(PLUGIN_NAME))
                .append("-")
                .append(manifest.getMainAttributes().getValue(VERSION))
                .append(NEWLINE).append(NEWLINE)
                .append("\t\t").append(manifest.getMainAttributes().getValue(DESCRIPTION))
                .append(NEWLINE).append(NEWLINE)
                .append("\t\t")
                .append("JVM虚拟机:").append(System.getProperty("java.vm.name")).append(",")
                .append("操作系统").append(System.getProperty("os.name")).append(",")
                .append("java版本").append(System.getProperty("java.version")).append(",")
                .append("运行时环境").append(System.getProperty("java.home")).append(",")
                .append("系统编码:").append(System.getProperty("file.encoding")).append(",")
                .append(NEWLINE).append(DIVIDER).toString();

        System.out.print(message);
    }
}
