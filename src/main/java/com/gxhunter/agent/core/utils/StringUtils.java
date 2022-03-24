package com.gxhunter.agent.core.utils;

import java.util.Random;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String METHOD_NAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz$_0123456789";

    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    public static String randomMethodName(int length) {
        int i = 0;
        if (i == length) {
            return "";
        }

        char[] buffer = new char[length];
        Random rnd = new Random();

        buffer[i++] = METHOD_NAME_CHARS.charAt(rnd.nextInt(54));
        while (i < length) {
            buffer[i++] = METHOD_NAME_CHARS.charAt(rnd.nextInt(64));
        }

        return new String(buffer);
    }
}
