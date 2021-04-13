package com.changingedu.imageupload;

import com.qingqing.common.domain.ImageChannel;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestImageSvc {
    public static final String originalFilename = "hdef_20200820094623.png";
    private static final String UPLOADPICTUREURLBASE = "/api/pi/v1/image/action/upload.json?channel=";
    public static final String imgSvcHost2 = "http://gateway.tst.idc.cedu.cn/imgsvc";
    public static final String imgSvcHost = "http://img-tst.changingedu.com/imgsvc";
    public static final String channel = "teacher_leave_apply_image";
    public static final String channel2 = "teacher_detect_auth";
    public static final String UTF8 = "UTF-8";

    public static void main(String[] args) throws Exception{
        File file = new File("/Users/hedefu/Documents/Developer/IntelliJ/JavaLearning/changingedu/src/main/resources/reimu_discontent.jpg");
        FileInputStream fis = new FileInputStream(file);
        int available = fis.available();
        byte[] bytes = new byte[available];
        fis.read(bytes);
        // while ((len = fis.read(bytes)) != -1){
        //
        // }
        String pictureUrl = "";
        String uploadUrl = imgSvcHost2 + UPLOADPICTUREURLBASE + channel;
        System.out.println(uploadUrl);
        try {
            pictureUrl = HttpUtil.postImageHttp(uploadUrl, UTF8, originalFilename, bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(pictureUrl);
    }

    @Test
    public void testImageUpload() throws Exception {
        File file = new File("src/main/resources/hdef_20200820094623.png");
        FileInputStream fis = new FileInputStream(file);
        int available = fis.available();
        byte[] bytes = new byte[available];
        fis.read(bytes);
        // while ((len = fis.read(bytes)) != -1){
        //
        // }
        String pictureUrl = "";
        String uploadUrl = imgSvcHost + UPLOADPICTUREURLBASE + channel2;
        System.out.println(uploadUrl);
        try {
            pictureUrl = HttpUtil.postImageHttp(uploadUrl, UTF8, originalFilename, bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(pictureUrl);
    }

    @Test
    public void test39() {
        System.out.println(ImageChannel.teacher_detect_auth);
    }
}
