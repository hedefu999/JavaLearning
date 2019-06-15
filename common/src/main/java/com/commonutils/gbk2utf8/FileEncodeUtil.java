package com.commonutils.gbk2utf8;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileEncodeUtil {
    private static boolean isFind = false;
    private static String encoding;
    /**
     * 使用java探测/猜测文本文件的编码，可使用的工具jar：
     * juniversalchardet  jchardet
     * ref http://www.meilongkui.com/archives/473
     * TODO 功能未实现，待定
     * @param fileName
     * @param encode
     * @return
     * @throws Exception
     */
    public static boolean checkFileEncode(String fileName,String encode) throws Exception{
        File file = new File(fileName);
        if (!file.exists()){
            return false;
        }
        nsDetector nsDetector = new nsDetector();
        nsDetector.Init(charset -> {
            isFind = true;
            encoding = charset;
        });
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] bytes = new byte[1024];
        int len = 0;
        boolean done = false;
        boolean isAscii = true;
        while ((len = bis.read(bytes,0,bytes.length)) != -1){
            //TODO isAscii返回一次false就停止循环
            if (isAscii){
                isAscii = nsDetector.isAscii(bytes,len);
            }else if(!done){
                done = nsDetector.DoIt(bytes,len,false);
            }
        }
        nsDetector.DataEnd();
        if (isAscii){
            encoding = "ASCII";
            isFind = true;
        } else if (!isFind){
            String[] prob = nsDetector.getProbableCharsets();
            if (prob.length > 0){
                //在没有发现的情况下，取第一个可能的编码
                encoding = prob[0];
            }
        }
        System.out.println("最终确定的编码："+encoding);
        return encoding.equalsIgnoreCase(encode);
    }

    /**
     * 对于windows平台文件的GBK编码无法识别，mac平台的GBK文件识别为KOI8-R
     * 不具备使用性。。。
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getFileEncode(String fileName) throws Exception{
        byte[] bytes = new byte[4096];
        InputStream is = Files.newInputStream(Paths.get(fileName));
        UniversalDetector detector = new UniversalDetector();
        int nread;
        while ((nread = is.read(bytes))>0 && !detector.isDone()){
            detector.handleData(bytes,0,nread);
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        return encoding;
    }
    //在mac平台生成的GBK文件可以正常转换，但windows平台的不可以
    public static void gbk2Utf8(String fileName) throws Exception{
        StringBuilder strBuilder = new StringBuilder();
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis,"GBK");
        BufferedReader reader = new BufferedReader(isr);
        String fileLineContent = null;
        while ((fileLineContent = reader.readLine()) != null){
            System.out.println(fileLineContent);
            strBuilder.append(fileLineContent).append("\r\n");
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
        BufferedWriter writer = new BufferedWriter(osw);
        writer.write(strBuilder.toString());
        System.out.println("文件转写执行完毕，将关闭流");
        if (reader!=null){
            reader.close();
        }
        if (writer!=null){
            writer.close();
        }
        osw.close();isr.close();
        fos.close();fis.close();
    }

    /**
     * 递归转换文件编码
     * @param rootContainer
     */
    public static void getFileList(String rootContainer){
        File dir = new File(rootContainer);
        if (!dir.isDirectory()){
            throw new RuntimeException(rootContainer+"不是文件夹");
        }
        File[] files = dir.listFiles();
        if (files == null){
            System.out.println("目录"+dir+"下没有文件，不必递归");
            return;
        }
        for(File file:files){
            String fileName = file.getName();
            if (file.isDirectory()){
                getFileList(fileName);
            }else {
                if (fileName.endsWith(".java")){
                    try {
                        gbk2Utf8(fileName);
                    } catch (Exception e) {
                        System.out.println("文件"+fileName+"转换出错");
                        e.printStackTrace();
                    }
                }
                if (fileName.endsWith(".classpath")||fileName.endsWith(".project")){
                    file.delete();
                }
            }
        }
    }
}
