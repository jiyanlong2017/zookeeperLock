package com.sinosoft.microservice.common.jpa;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数类型
 */
public enum SearchType {
    String, Integer, Long, Float, Double, BigDecimal, BigInteger, Boolean, Date, Timestamp;

    /**
     * 根据传入类型、传入操作符对传入值进行转换，如果传入值与传入类型不匹配，那么忽略
     *
     * @param strArray 传入的值数组
     * @param type     传入值对应的类型
     * @param operator 操作符
     * @return 根据传入类型对传入值进行转换的结果值
     */
    public static Object[] convert(String[] strArray, SearchType type,
                                   String operator) {
        if (ArrayUtils.isEmpty(strArray)) {
            return null;
        }

        List<Object> list = new ArrayList<Object>();

        for (int i = 0, len = strArray.length; i < len; i++) {
            String str = strArray[i];
            if (StringUtils.isBlank(str)) {
                continue;
            }

            switch (type) {
                case Integer:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createInteger(str));
                    }
                    break;
                case Long:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createLong(str));
                    }
                    break;
                case Float:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createDouble(str));
                    }
                    break;
                case Double:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createDouble(str));
                    }
                    break;
                case BigDecimal:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createBigDecimal(str));
                    }
                    break;
                case BigInteger:
                    if (NumberUtils.isNumber(str)) {
                        list.add(NumberUtils.createBigInteger(str));
                    }
                    break;
                case Boolean:
                    list.add(java.lang.Boolean.valueOf(str));
                    break;
                case Date:
                    //Joda DateTime
                    try {
                        DateTime dt = DateTime.parse(str);

                        if (str.length() <= 10 && StringUtils.equals(operator, SearchConst.LTE)) {
                            dt = getLTEDateTime(dt);
                        }

                        list.add(dt.toDate());
                    } catch (Exception ex) {

                    }
                    break;
                case Timestamp:
                    //java.sql.Timestamp
                    try {
                        DateTime dt = DateTime.parse(str);

                        if (str.length() <= 10 && StringUtils.equals(operator, SearchConst.LTE)) {
                            dt = getLTEDateTime(dt);
                        }
                        list.add(new java.sql.Timestamp(dt.getMillis()));
                    } catch (Exception ex) {

                    }
                    break;
                default:
                    list.add(str);
            }
        }

        return list.toArray();
    }

    private static DateTime getLTEDateTime(DateTime dt) {
        //如果是小于等于某天，那么需要截至到某天的24：00
        //处理为：日期加1天，再减1毫秒，即某天的截至某天的最后1毫秒前
        dt = dt.plusDays(1);
        dt = dt.minusMillis(1);

        return dt;
    }
}

