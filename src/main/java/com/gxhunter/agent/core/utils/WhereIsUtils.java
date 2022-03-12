package com.gxhunter.agent.core.utils;

import com.gxhunter.agent.core.Launcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WhereIsUtils {
    public static URI getJarURI() throws IllegalAccessException, URISyntaxException {
        URL url = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
        if (null != url) {
            return url.toURI();
        }

        String resourcePath = "/288daf08f4ba46dfde71b7f0624b0ad7f234a67a.txt";
        url = Launcher.class.getResource(resourcePath);
        if (null == url) {
            throw new IllegalAccessException("Can not locate resource file.");
        }

        String path = url.getPath();
        if (!path.endsWith("!" + resourcePath)) {
            throw new IllegalAccessException("Invalid resource path.");
        }

        path = path.substring(0, path.length() - resourcePath.length() - 1);

        return new URI(path);

    }
}
