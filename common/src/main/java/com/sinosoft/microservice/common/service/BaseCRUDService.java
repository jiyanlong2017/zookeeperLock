package com.sinosoft.microservice.common.service;


import com.sinosoft.microservice.common.jpa.SearchConst;
import com.sinosoft.microservice.common.jpa.SearchFilter;
import com.sinosoft.microservice.common.jpa.UniversalSearch;
import com.sinosoft.microservice.common.repository.BaseJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 实现CRUD/List等通用方法的服务基类，提供常规的CRUD支持
 * 此基类未考虑缓存，如果需要对Service进行缓存处理，那么最好单独编写Service，不用再继承此基类，
 * 只需继承BaseService，并把本类中实现的方法拷入即可
 */
@Service
public abstract class BaseCRUDService<T, ID extends Serializable> extends BaseService {

    /**
     * 自动注入合适的Dao（对应类型T，继承自BaseRepository<T, ID>的某个实现）
     * 如果子类需要使用具体的dao，那么将baseDao转换为具体的dao即可(不能在构造器里进行转换)
     * 更简洁的方法是子类里直接使用AutoWired再注入具体的dao即可
     */
    @Autowired
    protected BaseJpaRepository<T, ID> baseDao;

    /**
     * 根据主键获取实体
     *
     * @param id
     * @return
     */
    public T get(ID id) {
        return baseDao.findOne(id);
    }

    /**
     * 根据多个主键获取实体列表
     *
     * @param ids
     * @return
     */
    public Iterable<T> get(Iterable<ID> ids) {
        return baseDao.findAll(ids);
    }

    /**
     * 根据指定字段及值，返回符合条件的列表中的第一项
     * 此方法是一个帮助方法，用于替代在dao中频繁定义findByField这样的查找方法，但是性能会比在dao中定义的略差
     * 如果需要获取指定值的全部列表, 那么调用getListByField
     * @param fieldName 字段名（数据库表的字段名，非实体的字段名）
     * @param fieldValue 字段值
     * @return
     */
    public T getOneByField(String fieldName, Object fieldValue){
        Specification<T> spec = buildOneFieldSpecification(fieldName, fieldValue);

        //使用findOne的话，如果对应的spec在数据库里面有多条的话，会抛一个“存在多条记录”的异常，改成findAll，自己取第一条
        List<T> result =  baseDao.findAll(spec);

        if(result == null) {
            return null;
        }
        if(result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 新增
     *
     * @param t
     * @return
     */
    public T add(T t) {
        return baseDao.save(t);
    }

    /**
     * 批量新增
     *
     * @param entities
     * @return
     */
    public Iterable<T> add(Iterable<T> entities) {
        return baseDao.save(entities);
    }

    /**
     * 更新
     *
     * @param t
     * @return
     */
    public T update(T t) {
        return baseDao.save(t);
    }

    /**
     * 批量更新
     *
     * @param entities
     * @return
     */
    public Iterable<T> update(Iterable<T> entities) {
        return baseDao.save(entities);
    }

    /**
     * 根据主键删除
     *
     * @param id
     */
    public void delete(ID id) {
        baseDao.delete(id);
    }

    /**
     * 删除
     *
     * @param t
     */
    public void delete(T t) {
        baseDao.delete(t);
    }

    /**
     * 批量删除
     *
     * @param entities
     */
    public void delete(Iterable<? extends T> entities) {
        baseDao.delete(entities);
    }

    /**
     * 全部删除/整表删除
     */
    public void deleteAll(){
        baseDao.deleteAll();
    }

    /**
     * 记录总条数
     *
     * @return
     */
    public long count() {
        return baseDao.count();
    }

    /**
     * 是否存在指定ID的记录
     *
     * @param id
     * @return
     */
    public boolean exists(ID id) {
        return baseDao.exists(id);
    }

    /**
     * 返回全部数据，不分页
     *
     * @return
     */
    public List<T> getList() {
        return baseDao.findAll();
    }

    /**
     * 根据Specification返回数据列表
     * @param spec
     * @return
     */
    public List<T> getListBySpec(Specification<T> spec){
        return baseDao.findAll(spec);
    }

    /**
     * 返回全部数据，不分页,根据特定排序
     *
     * @return
     */
    public List<T> getListBySort(Sort sort) {
        return baseDao.findAll(sort);
    }

    /**
     * 返回全部数据，不分页,根据特定排序
     * 不进行权限过滤
     *
     * @param sortField 排序字段名称
     * @param sortOrder 排序方向，asc/desc
     * @return
     */
    public List<T> getListBySort(String sortField, String sortOrder) {
        if (StringUtils.isEmpty(sortField)) {
            return null;
        }
        if (StringUtils.isEmpty(sortOrder)) {
            sortOrder = "asc";
        }
        sortOrder = sortOrder.toLowerCase();
        Sort.Direction direction = Sort.Direction.ASC;
        if (StringUtils.equals(sortOrder, "desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = new Sort(direction, sortField);
        return baseDao.findAll(sort);
    }

    /**
     * 根据指定字段及值，返回符合条件的列表
     * 此方法是一个帮助方法，用于替代在dao中频繁定义findByField这样的查找方法，但是性能会比在dao中定义的略差
     * @param fieldName 字段名（数据库表的字段名，非实体的字段名）
     * @param fieldValue 字段值
     * @return
     */
    public List<T> getListByField(final String fieldName, final Object fieldValue){
        Specification<T> spec = buildOneFieldSpecification(fieldName, fieldValue);

        return baseDao.findAll(spec);
    }

    /**
     * 多字段查询，返回符合条件的列表，注意字段名和字段值一一对应
     * 例子:getListByFields(new String[]{"name", "deleteflag"}, new Object[]{"Jack", false})
     *
     * @param fieldNames 字段名集合
     * @param fieldValues 字段值集合
     * @return
     */
    public List<T> getListByFields(final  String[] fieldNames, final Object[] fieldValues){
        SearchFilter group = SearchFilter.buildAndGroup();
        for(int i = 0; i < fieldNames.length; i++){
            group.addFilter(fieldNames[i], fieldValues[i], SearchConst.EQ);
        }
        return getListBySearchFilter(group);
    }

    /**
     * 构建只有一个查询字段的Specification，操作符：相等
     * @param fieldName
     * @param fieldValue
     * @return
     */
    private Specification<T> buildOneFieldSpecification(final String fieldName, final Object fieldValue) {
        return new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Path expression = root.get(fieldName);

                    return cb.equal(expression, fieldValue);
                }
            };
    }

    /**
     * 返回列表数据，分页，排序，没有过滤条件
     * 不进行权限过滤
     *
     * @param pageRequest
     * @return
     */
    public Page<T> getListByPage(Pageable pageRequest) {
        return baseDao.findAll(pageRequest);
    }

    /**
     * 返回列表数据，不分页，默认排序， 根据specification进行数据过滤
     * @param searchFilter
     * @return
     */
    public List<T> getListBySearchFilter(SearchFilter searchFilter){
        //外部过滤组是否是一个空组
        boolean isEmptyFilter = SearchFilter.isEmptyFilterGroup(searchFilter);

        List<T> list;
        if (isEmptyFilter) {
            list = baseDao.findAll();
        }else {
            Specification<T> specification = UniversalSearch.build(searchFilter);
            list = baseDao.findAll(specification);
        }
        return list;
    }

    /**
     * 返回列表数据，不分页，可以排序， 根据specification进行数据过滤
     * @param searchFilter
     * @return
     */
    public List<T> getListBySearchFilter(SearchFilter searchFilter, Sort sort){
        //外部过滤组是否是一个空组
        boolean isEmptyFilter = SearchFilter.isEmptyFilterGroup(searchFilter);

        List<T> list;
        if (isEmptyFilter) {
            list = baseDao.findAll();
        } else {
            Specification<T> specification = UniversalSearch.build(searchFilter);
            list = baseDao.findAll(specification, sort);
        }
        return list;
    }

    /**
     * 返回列表数据，分页，排序， 根据specification进行数据过滤
     * 不进行权限过滤
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    public Page<T> getListByPage(Pageable pageRequest, Specification<T> specification) {
        return baseDao.findAll(specification, pageRequest);
    }



    /**
     * 根据Controller传过来的分页对象和过滤组来获取列表
     * 不进行权限过滤
     *
     * @param pageRequest
     * @param searchFilter
     * @return
     */
    public Page<T> getListByPage(Pageable pageRequest, SearchFilter searchFilter) {
        //外部过滤组是否是一个空组
        boolean isEmptyFilter = SearchFilter.isEmptyFilterGroup(searchFilter);

        Page<T> list;
        if (isEmptyFilter) {
            list = baseDao.findAll(pageRequest);
        }else {
            Specification<T> specification = UniversalSearch.build(searchFilter);
            list = baseDao.findAll(specification, pageRequest);
        }
        return list;
    }

    /**
     * 根据Controller传过来的分页对象和过滤组来获取列表
     * 同时会添加权限过滤条件，进行权限过滤
     *
     * @param pageRequest
     * @param searchFilter
     * @return
     */
  /*  public Page<T> getListByPageWithPermission(Pageable pageRequest, SearchFilter searchFilter) {

        //1.如果是管理员或者MenuKey为空，那么不进行任何权限过滤，只使用外部过滤条件searchFilter
        String menuKey = searchFilter.getMenuKey();
        if (isAdmin() || StringUtils.isEmpty(menuKey)) {
            return getListByPage(pageRequest, searchFilter);
        }

        //2.如果没有权限角色，那么返回空列表（正常情况下，没有角色，就看不到菜单，就进入不了这里）
        List<AppRole> appRoleList = getCurrentUser().appRoleList;
        if (appRoleList.size() == 0) {
            return getEmptyPage();
        }

        //3.如果没有对应的权限菜单对象，那么返回空列表(正常情况下，没有角色，就看不到菜单，就进入不了这里）
        AppRole.AppPermMenu appPermMenu = AppRole.getComplexAppPermMenuByMenuKey(appRoleList, menuKey);
        if (appPermMenu == null)
            return getEmptyPage();

        //4.如果权限菜单中设定能查看全部数据，那么不进行任何权限过滤，只使用外部过滤条件searchFilter
        if (appPermMenu.getAlldataflag()) {
            return getListByPage(pageRequest, searchFilter);
        }

        //----------------开始获取人头/即能查看那些人头的数据----------------------------
        //5.获取所有自定义用户列表（包含自定义组织机构下面的全部用户）
        List<Sys_User> customAllUserList = appPermMenu.customAllUserList;

        if (appPermMenu.getCurrentorgaflag()) {
            //如果还能查看当前部门，那么再加入当前部门的用户
            List<Sys_User> currentOrgaUserList = getCurrentUser().currentOrgaUserList;
            if (currentOrgaUserList.size() > 0)
                customAllUserList.addAll(currentOrgaUserList);
        }

        //6.将所有的用户转成ID列表，用于后面的IN条件语句构建
        List<String> allIds = new ArrayList<String>();
        for (Sys_User user : customAllUserList) {
            allIds.add(user.getId());
        }
        //默认还需要查看自己的数据，把自己的ID也要加入
        allIds.add(getCurrentUser().id);

        /*//***************************真正开始附加权限过滤条件******************************
        String idField = "userid";
        SearchFilter allFilterGroup;

        // where userid in {人头列表}
        //构建权限过滤组，里面只包含一个过滤条件IN，对应的数据库表中必须包含“userid”字段
        SearchFilter permissionFilterGroup = SearchFilter.buildAndGroup();
        permissionFilterGroup.addFilter(idField, allIds, SearchConst.IN);

        //外部过滤组是否是一个空组
        boolean isEmptyFilter = SearchFilter.isEmptyFilterGroup(searchFilter);
        if (isEmptyFilter) {
            //如果外部过滤组为空，那么直接使用权限过滤组
            allFilterGroup = permissionFilterGroup;
        } else {
            //如果外部过滤组不为空，那么组合外部过滤组和权限过滤组
            //where (外部过滤组) and (权限过滤组）
            allFilterGroup = SearchFilter.buildAndGroup();//AND
            allFilterGroup.addChildFilterGroup(searchFilter);
            allFilterGroup.addChildFilterGroup(permissionFilterGroup);
        }
        /*//******************************************************************************

        Specification<T> specification = UniversalSearch.build(allFilterGroup);
        Page<T> list = baseDao.findAll(specification, pageRequest);

        return list;
    }*/

    /**
     * 获取一个空Page
     *
     * @return
     */
    private Page<T> getEmptyPage() {
        return new Page<T>() {
            @Override
            public Iterator<T> iterator() {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<T> getContent() {
                return new ArrayList<T>();
            }

            @Override
            public boolean hasContent() {
                return true;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
                return null;
            }
        };
    }
}
