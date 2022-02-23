package com.ednow.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextReader {
    public static String InputStreamToString(InputStream input) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuffer message=new StringBuffer();
            String line = null;
            while((line = br.readLine()) != null) {
                message.append(line);
            }
            return message.toString();
        } catch (IOException exception) {
            return null;
        }

    }
}
