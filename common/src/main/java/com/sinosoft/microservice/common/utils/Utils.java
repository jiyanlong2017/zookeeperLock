package com.sinosoft.microservice.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目通用功能类
 */
public class Utils {
//    /**
//     * 用户密码加密MD5
//     */
//    public static String getMD5(String key) {
//        if (StringUtils.isEmpty(key))
//            return key;
//
//        String newKey = key;
//        try {
//            newKey = DigestUtils.md5Hex(key);
//        } catch (Exception ex) {
//
//        }
//        return newKey;
//    }

    /**
     * 对字符串进行MD5加密
     * @param val
     * @return
     */
    public static String getMD5(String val) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(val.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 拆分集合
     *
     * @param <T>
     * @param sourceList 要拆分的集合
     * @param count      每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> sourceList, int count) {

        if (sourceList == null || count < 1) {
            return null;
        }
        List<List<T>> resultList = new ArrayList<List<T>>();

        int size = sourceList.size();
        if (size <= count) { //数据量不足count指定的大小
            resultList.add(sourceList);
        } else {
            int pre = size / count;
            int last = size % count;

            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(sourceList.get(i * count + j));
                }
                resultList.add(itemList);
            }

            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(sourceList.get(pre * count + i));
                }
                resultList.add(itemList);
            }
        }

        return resultList;
    }



    public static String getClientIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
//
//    public static void main(String[] args) throws Exception {
////        String pwd = "1234";
////        String pwd2 = Utils.getMD5(pwd);
////        System.out.println(pwd2);
//
//        List<String> resList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
//                                             "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
//                                             "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37",
//                                             "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
//                                             "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61",
//                                             "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73",
//                                             "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85",
//                                             "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97",
//                                             "98", "99", "100", "101", "102");
//        List<List<String>> ret = split(resList, 50);
//
//        for (int i = 0; i < ret.size(); i++) {
//            System.out.println(ret.get(i));
//        }
//    }


}
