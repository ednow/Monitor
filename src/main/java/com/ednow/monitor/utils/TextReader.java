package com.ednow.monitor.utils;

import java.io.*;

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
