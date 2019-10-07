package com.wechatx.nessaries;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * @description: socket传输信息加密类
 * @date:
 * @auther: Tao
 *
*/
public class CodecUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtils.class);

    private static final String SALT = "dasouche-ceres-niubility";
    private static final String CLIENT_CRYPT_VERSION_PRE = "1.0.0|";

    /**
     * 加密
     *
     * @param id        微信id
     * @param extraData 额外数据(目前只有 微信间谍版本号)
     * @param timestamp 时间戳
     * @param data      源数据
     */
    public static byte[] javaEncode(String id, String extraData, long timestamp, byte[] data) {
        if(StringUtils.isAnyBlank(id,extraData) || ArrayUtils.isEmpty(data)){
            throw new RuntimeException("参数异常");
        }

        String key = id + extraData;
        byte[] md5key = generateAesKey(key);

        byte[] iv = generateIv(timestamp);
        byte[] encrypt = AESUtil.Encrypt(data, md5key, iv);

        String desCStr = CLIENT_CRYPT_VERSION_PRE + timestamp;
        byte[] desC = new byte[0];
        try {
            desC = desCStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("CodecUtils javaEncode error",e);
        }
        byte[] result = concatData(desC, encrypt);
        return result;
    }

    /**
     * 解密
     *
     * @param id        微信Id
     * @param extraData 额外数据(目前只有 微信间谍版本号)
     * @param data      源数据
     */
    public static String javaDecode(String id, String extraData, byte[] data) {
        if(StringUtils.isAnyBlank(id,extraData) || ArrayUtils.isEmpty(data)){
            throw new RuntimeException("参数异常");
        }

        String key = id + extraData;
        byte[] md5key = generateAesKey(key);

        byte[] desDataLenC = new byte[8];
        System.arraycopy(data, 0, desDataLenC, 0, 8);
        long desDataLen = TypeConvertUtils.BytesToLong(desDataLenC);

        byte[] desDataC = new byte[(int) desDataLen];
        System.arraycopy(data, 8, desDataC, 0, (int) desDataLen);
        String desData = null;
        try {
            desData = new String(desDataC, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("CodecUtils javaDecode error",e);
        }

        byte[] enDataLenC = new byte[8];
        System.arraycopy(data, 8 + (int) desDataLen, enDataLenC, 0, 8);
        long enDataLen = TypeConvertUtils.BytesToLong(enDataLenC);

        byte[] enDataC = new byte[(int) enDataLen];
        System.arraycopy(data, 16 + (int) desDataLen, enDataC, 0, (int) enDataLen);

        String[] splitData = desData.split("\\|");
        String timestamp;
        if (splitData.length > 1) {
            String version = splitData[0];
            timestamp = splitData[1];

            byte[] timestampC = TypeConvertUtils.longToBytes(Long.valueOf(timestamp));
            byte[] ivC = new byte[16];

            System.arraycopy(timestampC, 0, ivC, 0, 8);
            System.arraycopy(timestampC, 0, ivC, 8, 8);

            String result = AESUtil.Decrypt(enDataC, md5key, ivC);
            return result;
        } else {
            return "";
        }
    }


    /**
     * 加密 socket 数据时
     * 生成 AES key
     */
    private static byte[] generateAesKey(String key) {
        // 随机加盐
        String saltPassword = key + SALT;
        return Md5Util.md5BYtes(saltPassword);
    }


    /**
     * 生成 AES 向量
     * 向量的生成方式: 使用传入的时间戳专程long型(8byte)，然后以重复的方式的扩展(也就是2倍)为16byte
     * @param timestamp
     */
    private static byte[] generateIv(long timestamp) {
        byte[] bytes = TypeConvertUtils.longToBytes(timestamp);
        byte[] bytes16 = new byte[16];
        System.arraycopy(bytes, 0, bytes16, 0, bytes.length);
        System.arraycopy(bytes, 0, bytes16, bytes.length, bytes.length);
        return bytes16;
    }

    /**
     * byte 数据拼装
     数据准备: 输入数据byte[] IuputD1, 输入数据byte[] IuputD2, 输出数据byte[] OutputD(以此类推)
     拼接方式:
     OutputD[0~7] = IuputD1.length
     OutputD[8~(IuputD1.length+7)] = IuputD1
     OutputD[(IuputD1.length+8)~(IuputD1.length+15)] =  IuputD2.length
     OutputD[(IuputD1.length+16)~(IuputD1.length+IuputD2.length+15)] = IuputD2
     */
    private static byte[] concatData(byte[] desC,byte[] data){
        byte[] outputC = new byte[16 + desC.length + data.length];
        byte[] desLenC = TypeConvertUtils.longToBytes(desC.length);
        System.arraycopy(desLenC, 0, outputC, 0, 8);
        System.arraycopy(desC, 0, outputC, 8, desC.length);

        byte[] enDataLenC = TypeConvertUtils.longToBytes(data.length);
        System.arraycopy(enDataLenC, 0, outputC, 8 + desC.length, 8);
        System.arraycopy(data, 0, outputC, 16 + desC.length,
                data.length);

        return outputC;
    }

    public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
        String id = "wxid_jkr2y9iyrtv522";
        String extraData = "1.5.1";
        long timespamp = 1560852583298L;
        byte[] data = String.valueOf("\"123456\"").getBytes();
        byte[] bytes = javaEncode(id, extraData, timespamp,data);
        System.out.println(TypeConvertUtils.bytesToHex(bytes));
        String a = "0000000000000013312e352e317c31353630383532353833323938000000000000001004ac3681ec0ef61c9e80b1f144016537";
        System.out.println(javaDecode(id,extraData,TypeConvertUtils.hexToBytes(a)));
        System.out.println(javaDecode(id,extraData,bytes));
    }

}
