package com.sinosoft.microservice.common.jpa;

/**
 * 操作符常量
 * 前端传过来的一般都是字符串，为了方便转换，直接使用字符串作为操作符
 */
public class SearchConst {

    /**
     * 逻辑与
     */
    public final static String LOGIC_AND = "and";

    /**
     * 逻辑或
     */
    public final static String LOGIC_OR = "or";

    //    case "eq":
//    case "neq":
//    case "gt":
//    case "gte":
//    case "lt":
//    case "lte":
//    case "startswith":
//    case "endswith":
//    case "contains":
//    case "doesnotcontain":

    /**
     * 等于
     */
    public final static String EQ = "eq";

    /**
     * 不等于
     */
    public final static String NOT_EQ = "neq";

    /**
     * 大于
     */
    public final static String GT = "gt";

    /**
     * 大于等于
     */
    public final static String GTE = "gte";

    /**
     * 小于
     */
    public final static String LT = "lt";

    /**
     * 小于等于
     */
    public final static String LTE = "lte";

    /**
     * 开始于，= like "%..."
     */
    public final static String START_WITH = "startswith";

    /**
     * 结束于，= like "...%"
     */
    public final static String END_WITH = "endswith";

    /**
     * 包含，= like
     */
    public final static String CONTAINS = "contains";

    /**
     * 不包含，= not like
     */
    public final static String NOT_CONTAINS = "doesnotcontain";

    /**
     * like
     */
    public final static String LIKE = "like";

    /**
     * not like
     */
    public final static String NOT_LIKE = "nlike";

    /**
     * in
     */
    public final static String IN = "in";

    /**
     * between
     */
    public final static String BETWEEN = "btw";
    /**
     * isnull
     */
    public  final static String ISNULL ="isnull";
}
