package com.sinosoft.microservice.common.jpa;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 动态查询的过滤条件封装类，内部可以包含多层嵌套
 * 1.当logic不为空，filters不为空时，代表这是一个过滤条件组，filters为包含的全部过滤条件，使用logic定义的逻辑操作组合到一起
 * 2.当logic为空，filters为空时，代表这是一个过滤条件，使用field/operator/value/ignoreCase这4个字段
 * 3.把组和条件封装到一起，是为了方便递归操作
 */
public class SearchFilter implements Serializable {

    private static final long serialVersionUID = 5594782286944577488L;

    //过滤条件组逻辑操作：and/or
    private String logic;
    //过滤条件组的全部条件集合
    private List<SearchFilter> filters;

    //条件字段
    private String field;
    //条件值
    private Object value;
    //条件操作符：eq/like....
    private String operator;
    //是否忽略大小写
    private boolean ignoreCase = true;

    //结合Shiro权限过滤时，填入当前操作的菜单的Key，用于后台进行数据范围权限返回查找
    //只有需要进行数据过滤的业务，才需要用到此字段，否则会出错
    private String menuKey;

    private SearchFilter() {
    }

    /**
     * 创建一个And过滤条件组
     * @return
     */
    public static SearchFilter buildAndGroup(){
        return new SearchFilter(SearchConst.LOGIC_AND, new ArrayList<SearchFilter>());
    }


    /**
     * 创建一个Or条件过滤组
     * @return
     */
    public static SearchFilter buildOrGroup(){
        return new SearchFilter(SearchConst.LOGIC_OR, new ArrayList<SearchFilter>());
    }

    /**
     * 创建一个过滤条件组
     * @param logic
     * @param filters
     */
    public SearchFilter(String logic, List<SearchFilter> filters) {
        this.logic = logic;
        this.filters = filters;
    }

    /**
     * 当前组再加入一个过滤条件组
     * @param filterGroup
     */
    public void addChildFilterGroup(SearchFilter filterGroup){
        if(!filterGroup.isFilterGroup()) {
            return;
        }

        if(!isFilterGroup()) {
            return;
        }

        this.getFilters().add(filterGroup);
    }

    /**
     * 当前组加入一个Filter
     * @param field
     * @param value
     * @param operator
     */
    public void addFilter(String field, Object value, String operator){
        if(!isFilterGroup()) {
            return;
        }

        SearchFilter filter = new SearchFilter(field, value, operator);
        this.getFilters().add(filter);
    }

    /**
     * 当前组加入一个Filter，忽略大小写
     * @param field
     * @param value
     * @param operator
     * @param ignoreCase
     */
    public void addFilter(String field, Object value, String operator, boolean ignoreCase){
        if(!isFilterGroup()) {
            return;
        }

        SearchFilter filter = new SearchFilter(field, value, operator, ignoreCase);
        this.getFilters().add(filter);
    }

    /**
     * 当前组加入一个已经构建好的条件Filter
     * @param filter
     */
    public void addFilter(SearchFilter filter){
        if(!isFilterGroup()) {
            return;
        }

        this.getFilters().add(filter);
    }

    /**
     * 创建一个过滤条件
     * @param field
     * @param value
     * @param operator
     */
    public SearchFilter(String field, Object value, String operator) {
        this.field = field;
        this.value = value;
        this.operator = operator;
        this.ignoreCase = true;
    }

    /**
     * 创建一个过滤条件
     * @param field
     * @param value
     * @param operator
     * @param ignoreCase
     */
    public SearchFilter(String field, Object value, String operator, boolean ignoreCase) {
        this.field = field;
        this.value = value;
        this.operator = operator;
        this.ignoreCase = ignoreCase;
    }

    /**
     * 当前SearchFilter是否是过滤条件组
     * @return
     */
    public boolean isFilterGroup(){
        if(!StringUtils.isEmpty(logic)){
            return true;
        }

        return false;
    }

    /**
     * 判断某个SearchFilter是否是空的SearchFilter组
     * 帮助方法，用于外部判断
     * @param filter
     * @return
     */
    public static boolean isEmptyFilterGroup(SearchFilter filter){
        if(filter == null) {
            return true;
        }

        if (StringUtils.isEmpty(filter.getLogic())) {
            return true;
        }

        if(filter.getFilters() == null) {
            return true;
        }

        if(filter.getFilters().size() == 0) {
            return true;
        }

        return false;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<SearchFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<SearchFilter> filters) {
        this.filters = filters;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 因为value可能是一个Object[]
     * @return
     */
    public Object getSingleValue(){
        if(value instanceof Object[]){
            Object[] objs = (Object[])value;
            return objs[0];
        }

        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String getMenuKey() {
        return menuKey;
    }

    /**
     * 结合Shiro权限过滤数据时，填入当前操作的菜单的Key，用于后台进行数据范围权限返回查找
     * 只有需要进行数据范围过滤的业务，才需要用到此字段，否则会出错
     * @param menuKey
     */
    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }


    //-----------------用于解析RequestParams的帮助方法----------------
    /**
     * 解析前台参数组合，生成一个AND的SearchFilter的group
     * searchParams中key的格式为OPERATOR_FIELDNAME_TYPE
     */
    public static SearchFilter parse(Map<String, String[]> searchParams) {
        Map<String, SearchFilter> conditions = Maps.newHashMap();

        for (Map.Entry<String, String[]> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (ArrayUtils.isEmpty(values)) {
                continue;
            }

            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length < 2) {
                continue;
                //throw new IllegalArgumentException(key + " is not a valid search filter name");
            }

            //获取搜索操作符
            String operator = names[0].toLowerCase();

            //获取字段名称
            String filedName = names[1];

            //获取类型(如果不带类型，那么默认为String)
            SearchType type = SearchType.String;
            if (names.length >= 3) {
                type = SearchType.valueOf(names[2]);
            }

            Object[] ovalues = SearchType.convert(values, type, operator);
            if (ArrayUtils.isEmpty(ovalues)) {
                continue;
            }

            // 创建SearchCondition
            //如果传来的值就一个，那么直接取这个值放入；如果是多个，那么把整个值数组放入
            SearchFilter condition;
            if(ovalues.length == 1){
                condition = new SearchFilter(filedName, ovalues[0], operator);
            }else{
                condition = new SearchFilter(filedName, ovalues, operator);

            }
            conditions.put(key, condition);
        }

        //构建条件过滤组(And)
        SearchFilter group = SearchFilter.buildAndGroup();

        Collection<SearchFilter> values = conditions.values();
        for(SearchFilter filter:values){
            group.addFilter(filter);
        }

        return group;
    }
}
