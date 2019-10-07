package com.wechatx.nessaries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @description: AES加密解密
 * @date:
 * @auther: Tao
 *
*/
public class AESUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);

    /**
     * 加密
     * @param content
     * @param key
     * @param IV
     * @return
     * @throws Exception
     */
    public static byte[] Encrypt(byte[] content,byte[] key,byte[] IV) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ips = new IvParameterSpec(IV);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encrypted = cipher.doFinal(content);
            return encrypted;
        } catch (Exception e) {
            LOGGER.error("AESUtil Encrypt error :{}",e);
        }
        return null;
    }

    /**
     * 解密
     * @param content
     * @param key
     * @param IV
     * @return
     * @throws Exception
     */
    public static String Decrypt(byte[] content,byte[] key,byte[] IV) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] original = cipher.doFinal(content);
            String originalString = new String(original,"UTF-8");
            return originalString;
        } catch (Exception e) {
            LOGGER.error("AESUtil Decrypt error :{}",e);
            return null;
        }
    }
}
