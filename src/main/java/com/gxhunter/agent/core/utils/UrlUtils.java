package com.gxhunter.agent.core.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    public static Map<String,String> getParam(URI uri) {
        Map<String, String> result = new HashMap<>();
        if (uri.getQuery()==null||uri.getQuery().isEmpty()) {
            return result;
        }
        for (String paramEntry : uri.getQuery().split("&")) {
            String[] split = paramEntry.split("=");
            result.put(split[0], split[1]);
        }
        return result;
    }
}
