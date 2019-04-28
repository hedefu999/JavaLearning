package com.mybatis.utils;

import java.io.File;
import java.io.FileInputStream;

public class FileUtils {
    public static byte[] readFile(String filePath){
        File file = new File(filePath);
        long fileSize = file.length();
        byte[] bytes = new byte[(int) fileSize];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
