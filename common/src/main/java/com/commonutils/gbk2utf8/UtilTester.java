package com.commonutils.gbk2utf8;

import org.junit.Test;

import java.io.File;

public class UtilTester {
    @Test
    public void testFileEncodingDetector()throws Exception{
        String fileName = "src/main/java/com/commonutils/gbk2utf8/IUserBiz.txt";
        String encoding = FileEncodeUtil.getFileEncode(fileName);
        System.out.println(encoding);
        //System.out.println(new File("").getCanonicalPath());
        //FileEncodeUtil.gbk2Utf8(fileName);

    }
}
