package com.sinosoft.microservice.common.jpa;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA万能动态查询类
 */
public class UniversalSearch {
    /**
     * 构建动态查询条件组
     * @param filterGroup    过滤条件组，必须是组，不能是单个条件
     * @param <T>
     * @return
     */
    public static <T> Specification<T> build(final SearchFilter filterGroup) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                if(SearchFilter.isEmptyFilterGroup(filterGroup)){
                    return cb.conjunction();
                }

//                if (filterGroup == null) {
//                    return cb.conjunction();
//                }
//
//                if (StringUtils.isEmpty(filterGroup.getLogic())) {
//                    return cb.conjunction();
//                }
//
//                if (Collections3.isEmpty(filterGroup.getFilters())) {
//                    return cb.conjunction();
//                }

                return buildFilterGroup(filterGroup, root, query, cb);
            }
        };
    }

    /**
     * 将某个FilterGroup构建成Predicate，可以递归构建
     * @param filterGroup
     * @param root
     * @param query
     * @param cb
     * @param <T>
     * @return
     */
    private static <T> Predicate buildFilterGroup(SearchFilter filterGroup, Root<T> root, CriteriaQuery<?> query,
                                             CriteriaBuilder cb) {
        List<SearchFilter> childFilters = filterGroup.getFilters();
        List<Predicate> list = Lists.newArrayList();

        //将当前组内的filter都创建成对应的Predicate组合起来
        for (SearchFilter child : childFilters) {

            //如果组内某个filter还是一个组，那么递归创建，还是得出一个predicate
            if (child.isFilterGroup()) {
                Predicate groupPredicate = buildFilterGroup(child, root, query, cb);
                list.add(groupPredicate);
                continue;
            }

            Path expression = root.get(child.getField());
            if (StringUtils.equals(child.getOperator(), SearchConst.EQ)) {
                list.add(cb.equal(expression, child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.NOT_EQ)) {
                list.add(cb.notEqual(expression, child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.LIKE)) {
                list.add(cb.like(expression, "%" + child.getValue() + "%"));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.CONTAINS)) {
                list.add(cb.like(expression, "%" + child.getValue() + "%"));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.NOT_LIKE)) {
                list.add(cb.notLike(expression, "%" + child.getValue() + "%"));
            }else if (StringUtils.equals(child.getOperator(), SearchConst.NOT_CONTAINS)) {
                list.add(cb.notLike(expression, "%" + child.getValue() + "%"));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.START_WITH)) {
                list.add(cb.like(expression, child.getValue() + "%"));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.END_WITH)) {
                list.add(cb.like(expression, "%" + child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.GT)) {
                list.add(cb.greaterThan(expression, (Comparable) child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.LT)) {
                list.add(cb.lessThan(expression, (Comparable) child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.GTE)) {
                list.add(cb.greaterThanOrEqualTo(expression, (Comparable) child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.LTE)) {
                list.add(cb.lessThanOrEqualTo(expression, (Comparable) child.getValue()));
            } else if (StringUtils.equals(child.getOperator(), SearchConst.IN)) {
                //TODO:IN
                ArrayList arrayList = (ArrayList)child.getValue();
                list.add(expression.in(arrayList));
                //list.add(expression.in(filter.value));

            } else if (StringUtils.equals(child.getOperator(), SearchConst.BETWEEN)) {
                Object[] value = (Object[]) child.getValue();
                list.add(cb.between(expression, (Comparable) value[0],(Comparable) value[1]));
            } else if(StringUtils.equals(child.getOperator(), SearchConst.ISNULL)){
                list.add(cb.isNull(expression));
            }
        }

        if (list.isEmpty()) {
            return cb.conjunction();
        }

        if (StringUtils.equals(filterGroup.getLogic(), SearchConst.LOGIC_AND)) {
            return cb.and(list.toArray(new Predicate[list.size()]));
        }

        return cb.or(list.toArray(new Predicate[list.size()]));
    }
}
