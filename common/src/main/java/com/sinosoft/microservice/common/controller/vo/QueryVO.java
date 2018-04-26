package com.sinosoft.microservice.common.controller.vo;

/**
 * 用于H5交互的对象
 *
 *
 * String token = UUID.randomUUID().toString();
 * long time = new Date().getTime();
 * String s = token + "," + time;
 * String sign = Utils.getMD5(s);
 */
public class QueryVO {
    /**
     * timestamp
     */
    public String x1 = "";

    /**
     * userid
     */
    public String x2 = "";

    /**
     * sign
     */
    public String x3 = "";

    /**
     * 类型
     */
    public int type;

    /**
     * 子项目
     */
    public String projectId = "";


    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getX3() {
        return x3;
    }

    public void setX3(String x3) {
        this.x3 = x3;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
