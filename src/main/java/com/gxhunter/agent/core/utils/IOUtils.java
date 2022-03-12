package com.gxhunter.agent.core.utils;

import com.gxhunter.agent.core.AgentConst;

import java.io.*;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

/**
 *
 */
public class IOUtils implements AgentConst {
    public static InputStream download(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        return urlConnection.getInputStream();
    }

    public static boolean isReachable(String ip,int timeout) {
        try {
            return Inet4Address.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copy(InputStream in, OutputStream out) {
        try {
            int length;
            byte[] buffer = new byte[1024];
            while (-1 != (length = in.read(buffer))) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String doGet(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            try (InputStream inputStream = urlConnection.getInputStream()) {
                StringBuilder sb = new StringBuilder();
                readStream2String(inputStream, sb::append);
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        readStream2String(inputStream, sb::append);
        return sb.toString();

    }


    /**
     * 逐行读取字符串
     *
     * @param inputStream 输入流
     * @param consumer    消费者，每行回调一次
     */
    public static void readStream2String(InputStream inputStream, Consumer<String> consumer) {
        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
