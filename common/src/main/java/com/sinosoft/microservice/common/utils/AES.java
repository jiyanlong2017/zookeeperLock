package com.sinosoft.microservice.common.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * AES加解密算法
 */

public class AES {

    static {
        //"算法/模式/补码方式" 提前初始化一下,提升以后的加解密速度,内部单例?
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     * @param src   待加密的字符串
     * @param key   key
     * @param biv   向量
     * @return
     * @throws Exception
     */
    public static String Encrypt(String src, String key, byte[] biv) throws Exception {
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }

        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(biv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        //加密,注意UTF-8,解决中文乱码问题
        byte[] encrypted = cipher.doFinal(src.getBytes("utf-8"));

        //此处使用BASE64做转码功能，同时能起到2次加密的作用
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     * 解密
     * @param src   待解密的字符串
     * @param key   key
     * @param biv   向量
     * @return
     * @throws Exception
     */
    public static String Decrypt(String src, String key, byte[] biv) throws Exception {
        try {
            // 判断Key是否正确
            if (key == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes();

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(biv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //先用base64解码
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(src);
            try {
                //解密
                byte[] original = cipher.doFinal(encrypted1);

                //注意UTF-8,解决中文乱码问题
                return new String(original, "utf-8");
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

//    public static void main(String[] args) throws Exception {
//        //test1();
//
//        mockRequest();
//    }

    public static void test1() throws Exception {

        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
         * 此处使用AES-128-CBC加密模式，key需要为16位。
         */
        String cKey = "1234567890123456";
        String cKey2 = "0234567891234567";
        String cKey3 = "8234567893234567";

        // 需要加密的字串
        String cSrc = "中文邮件-测试Email : abcd1234@xxx.com";
        System.out.println(cSrc);

        // 加密1
        long lStart = System.currentTimeMillis();
        String enString = AES.Encrypt(cSrc, cKey, Consts.biv);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");

        // 加密2
        lStart = System.currentTimeMillis();
        String enString2 = AES.Encrypt(cSrc, cKey2, Consts.biv);
        System.out.println("加密2后的字串是：" + enString2);

        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密2耗时：" + lUseTime + "毫秒");

        // 加密3
        lStart = System.currentTimeMillis();
        String enString3 = AES.Encrypt(cSrc, cKey3, Consts.biv);
        System.out.println("加密3后的字串是：" + enString3);

        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密3耗时：" + lUseTime + "毫秒");

        // 解密
        lStart = System.currentTimeMillis();
        String DeString = AES.Decrypt(enString, cKey, Consts.biv);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");

    }

    private static void mockRequest(){
        String timestamp = "1389085779854";
        String phone = "18612345678";
        String token = "2f68eca6-f61d-4322-bb87-3f3c7f804b74";

        String skey1 = getSKey(timestamp);
        String sign1 = timestamp + "," + phone + "," + token;
        System.out.println(sign1);
        try {
            String sign2 = AES.Encrypt(sign1, skey1, Consts.biv);
            System.out.println("sign: ");
            System.out.println(sign2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TestObject testObject = new TestObject();
        testObject.setName("Tony张三");
        testObject.setAge(20);

        String jsonString = new JsonMapper().toJson(testObject);
        System.out.println("JsonString: ");
        System.out.println(jsonString);

        try {
            String content = AES.Encrypt(jsonString, getSKey(token), Consts.biv);
            System.out.println("content: ");
            System.out.println(content);

            String content2 = AES.Decrypt(content, getSKey(token), Consts.biv);
            System.out.println("content2:");
            System.out.println(content2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getSKey(String str){
        if(StringUtils.isEmpty(str)) {
            return null;
        }
        if(str.length()<16){
            str = str + str;
        }

        return str.substring(0, 16);
    }
}
