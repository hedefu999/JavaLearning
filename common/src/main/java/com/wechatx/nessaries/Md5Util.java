package com.wechatx.nessaries;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @description: MD5工具类
 * @date:
 * @auther: Tao
 *
*/
public class Md5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);

    /**
     * 获得md5串
     * @param input
     * @return
     */
    public static String md5(String input) {
        if(input == null){
            return null;
        }
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(input.getBytes());
            StringBuilder buffer = new StringBuilder();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("md5 error", e);
        }
        return null;
    }

    /**
     * 获得md5加密byte[]
     * @param sourceData
     * @return
     */
    public static byte[] md5BYtes(String sourceData) {
        if (sourceData == null) {
            return null;
        }

        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(sourceData.getBytes("UTF-8"));
            //获得加密后的数据
            return md.digest();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error("md5 error", e);
        }
        return null;
    }

}
