package com.sinosoft.microservice.common.utils;

/**
 * 常量
 */
public class Consts {
    public static final String PREFERENCE_NAME = "com.dd.dd.prefs";

    // 测试用的Json接口，返回郑州天气Json数据
    public static final String TEST_URL = "http://www.weather.com.cn/data/sk/101180101.html";

    public static final String APP_ENAME = "ds";

    public static final String APP_CNAME = "ds名字";

    //指定cache的文件目录名称
    public static final String CACHE_FILE_NAME = "ds";

    /**
     * 公司网站地址
     */
    public static final String COMPANY_WEB_SITE = "http://www.ds.com";

    /**
     * 基地址
     */
    public static final String BASE_URL = "http://192.168.0.1:8080/ds";



    /**
     * 服务条款地址
     */
    public static final String SERVICE_URL = BASE_URL + "";


    /**
     * 保密协议地址
     */
    public static final String SECURITY_URL = BASE_URL + "";





    public static byte[] biv = {2, 1, 0, 2, 1, 3, 5, 4, 1, 5, 2, 6, 2, 7, 1, 8};
    public static String HEADER1 = "X-HLG-Key";
    public static String HEADER2 = "X-HLG-Sign";

    //H5请求附带的参数名称
    public static String H5_X1 = "x1";
    public static String H5_X2 = "x2";
    public static String H5_X3 = "x3";
    //影像返回errorcode 成功
    public static String CMCLIENT_RESPONSE_SUCCESS_CODE = "0000";

}
