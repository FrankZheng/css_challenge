package com.frankzheng.css_challenge;

import java.io.*;

public class FileUtils {

    public static void readToBuffer(InputStream is, StringBuilder buffer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            buffer.append("\n");
            line = reader.readLine();
        }
        reader.close();
        is.close();
    }



}
