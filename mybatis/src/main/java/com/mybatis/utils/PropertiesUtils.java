package com.mybatis.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;

public class PropertiesUtils {
    /**
     * 2.通过Spring中的PropertiesLoaderUtils工具类进行获取 .loadAllProperties(filePath);
     * @param absolutePath
     * @return
     */
    public static Properties readPropertiesFromFile(String absolutePath){
        Properties properties = new Properties();
        File file = new File(absolutePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream in = new BufferedInputStream(fileInputStream);
            properties.load(in);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 绝对路径可在target/classes文件夹中查看,如log4j.properties位于classes下，相对路径就是 /log4j.properties
     * @param relativePath
     * @return
     */
    public static Properties readPropsFile(String relativePath){
        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtils.class.getResourceAsStream(relativePath);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
