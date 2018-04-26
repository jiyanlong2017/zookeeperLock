package com.sinosoft.microservice.common;


import java.io.Serializable;

/**
 * 服务端返回给客户端的数据封装对象
 */
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 2652608027682835212L;
    /**
     * 服务端业务逻辑是否执行成功
     */
    private boolean success;

    /**
     * 错误编号
     */
    private String errorCode = "";

    /**
     * 信息(如果发生错误，那么代表错误信息)
     */
    private String message = "";

    /**
     * 返回给客户端的数据对象
     */
    private Object data;

    public JsonResult() {
    }

    public JsonResult(boolean success, String errorCode, String message, Object data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

   public JsonResult(boolean success, String message, Object data) {
       this.success = success;
       this.message = message;
       this.data = data;
   }

    public JsonResult(boolean success, Object data){
        this.success = success;
        this.data = data;
    }

    /**
     * 返回一个代表成功的JsonResult，无返回对象，无信息
     *
     * @return 成功的JsonResult
     */
    public static JsonResult success() {
        return new JsonResult(true,null);
    }

    /**
     * 返回一个代表成功的JsonResult，包括返回的数据
     *
     * @param data 需要返回的数据
     * @return 成功的JsonResult
     */
    public static JsonResult success(Object data) {
        return new JsonResult(true, data);
    }

    /**
     * 返回一个代表成功的JsonResult，有返回对象，有提示信息
     *
     * @return 成功的JsonResult
     */
    public static JsonResult success(String message, Object data) {

        return new JsonResult(true, message, data);
    }

    /**
     * 出现未知错误,使用这个
     * 返回一个代表失败的JsonResult，包含错误信息
     *
     * @param message 需要返回的错误信息
     * @return 失败的JsonResult
     */
    public static JsonResult error(String message) {
        return new JsonResult(false, "", message, null);
    }

    /**
     * 返回一个代表失败的JsonResult，包含错误信息
     * @param data
     * @return
     */
    public static JsonResult error(Object data){
        return new JsonResult(false, data);
    }

    /**
     * 返回一个代表失败的JsonResult，包含错误码和错误信息
     *
     * @param errorCode 错误码
     * @param message   需要返回的错误信息
     * @return
     */
    public static JsonResult error(String errorCode, String message) {
        return new JsonResult(false, errorCode, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
