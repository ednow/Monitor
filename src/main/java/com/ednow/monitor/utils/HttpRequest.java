package com.ednow.monitor.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 来源 https://blog.csdn.net/longshehe9319/article/details/80509829
 */
public class HttpRequest {

    /**
     * @param requestUrl 请求地址
     * @param JSONString 发送的内容是json字符串格式
     * @param method POST/GET
     * @return
     */
    public static JSONObject sendJson(String requestUrl, String JSONString, String method){
        try {
            URL url = new URL(requestUrl); //url地址
            String resp = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(JSONString.getBytes(StandardCharsets.UTF_8));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuilder sbf = new StringBuilder();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), StandardCharsets.UTF_8);
                    sbf.append(lines);
                }
                resp = sbf.toString();

            }
            connection.disconnect();
            return (JSONObject) JSON.parse(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
