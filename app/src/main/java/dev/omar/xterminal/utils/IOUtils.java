package dev.omar.xterminal.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {


    public static String read(InputStream inputStream){
        if(inputStream == null)return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line).append("\n");
            }
        } catch (IOException e) {

        }
        return  sb.toString();
    }
}
