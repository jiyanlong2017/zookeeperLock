package com.sinosoft.microservice.common.controller;

import com.sinosoft.microservice.common.JsonResult;
import com.sinosoft.microservice.common.utils.AES;
import com.sinosoft.microservice.common.utils.Consts;
import com.sinosoft.microservice.common.utils.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller基类
 */
public class BaseController {

    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 获取当前Request
     *
     * @return HttpServletRequest
     */
    protected HttpServletRequest getCurrentRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }



    /**
     * 获取当前Session
     *
     * @return HttpSession
     */
    protected HttpSession getCurrentSession() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession();
        return session;
    }

    /**
     * 解密内容
     * @param content
     * @param key
     * @return 解密失败,返回空字符串
     */
    protected String decryptContent(String content, String key){
        String result = "";

        try {
            result = AES.Decrypt(content, key, Consts.biv);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * 加密JsonResult,转成字符串
     * @param jsonResult
     * @param key
     * @return 加密失败,返回空字符串
     */
    protected String encryptJsonResult(JsonResult jsonResult, String key){
        String result = "";

        JsonMapper mapper = new JsonMapper();
        String jsonString = mapper.toJson(jsonResult);
        try {
            result = AES.Encrypt(jsonString, key, Consts.biv);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return result;
    }
}
