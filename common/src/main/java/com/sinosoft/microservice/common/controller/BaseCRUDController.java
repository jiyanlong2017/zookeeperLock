package com.sinosoft.microservice.common.controller;


import com.sinosoft.microservice.common.BaseModel;
import com.sinosoft.microservice.common.JsonResult;

import com.sinosoft.microservice.common.service.BaseCRUDService;
import com.sinosoft.microservice.common.utils.BeanMapper;
import com.sinosoft.microservice.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 基本的列表、增删改查接口的Controller
 * 适用于有Grid及CRUD操作的页面
 * 接口如下(根路径以/test/ajaxgrid为例)：
 * 1.主页面    URL: /test/ajaxgrid GET 对应的jsp页面名称必须为index.jsp
 * 2.获取Grid数据的接口    /test/ajaxgrid/list GET ————>因为涉及到具体的Grid实现，所以放入不同Grid实现的子类中
 * 3.获取指定ID对象的接口    URL: /test/ajaxgrid/1 GET
 * 4.新增接口    URL：/test/ajaxgrid/add POST
 * 5.更新/修改接口    URL：/test/ajaxgrid/update POST
 * 6.删除接口 URL：/test/ajaxgrid/delete/1 POST
 */
public abstract class BaseCRUDController<T extends BaseModel<ID>, ID extends Serializable> extends BaseController {


    protected static Logger logger = LoggerFactory.getLogger(BaseCRUDController.class);


    /**
     * 自动注入合适的Service（对应类型T，BaseCRUDService<T, ID>的某个实现）
     * 如果子类需要使用具体的service，那么将baseService转换为具体的dao即可(不能在构造器里进行转换)
     * 更简洁的方法是子类里直接使用AutoWired再注入具体的service即可
     */
    @Autowired
    protected BaseCRUDService<T, ID> baseService;

    /**
     * 实体类型
     */
    //protected final Class<T> entityClass;

    private String viewPrefix;

    /**
     * 是否需要对grid数据进行数据范围筛选（即根据权限、角色进行数据过滤）
     */
    protected boolean isNeedToFilterGrid;

    /**
     * 如果需要进行数据范围筛选，那么需要传入一个menuKey，以用来做权限数据定位
     */
    protected String menuKey;

    protected void setNeedToFilterGrid(String menuKey) {
        this.isNeedToFilterGrid = true;
        this.menuKey = menuKey;
    }

    /**
     * 返回列表数据时，是否需要进行视图对象转换
     * 是的话，子类需要实现convertVO方法
     */
    protected boolean isNeedToConvertVO;

    protected void setNeedToConvertVO() {
        isNeedToConvertVO = true;
    }

   // protected abstract Object convertVO(T t);

    protected BaseCRUDController() {
//        this.entityClass = Reflections.getClassGenricType(getClass());
//
//        //获取默认的根url映射路径
//        setViewPrefix(defaultViewPrefix());
    }



    /**
     * 因为涉及到具体的Grid实现，所以放入不同Grid实现的子类中
     * 获取EasyUI Grid数据的接口
     * 返回页面上Grid所需要的列表数据(根路径以/test/ajaxgrid为例)
     * URL: /test/ajaxgrid/list GET
     */
    /*
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(EzPage ezPage, HttpServletRequest request) {
        //构建一个分页请求对象
        Pageable pageRequest = ezPage.buildJPAPageRequest();

        Map<String, String[]> params = Servlets.getSearchParameters(request);
        SearchFilter group = SearchFilter.parse(params);

        try {
            //获取分页结果
            Page<T> pageResult;

            //是否需要根据权限进行数据过滤
            if (isNeedToFilterGrid) {
                group.setMenuKey(menuKey);
                pageResult = baseService.getListByPageWithPermission(pageRequest, group);
            } else {
                pageResult = baseService.getListByPage(pageRequest, group);
            }

            //是否需要进行视图对象转换
            if (isNeedToConvertVO) {
                List<Object> list = new ArrayList<Object>();
                for (T t : pageResult.getContent()) {
                    Object vo = convertVO(t);
                    list.add(vo);
                }

                return EzPageResult.build(pageResult.getTotalElements(), list);
            }

            return EzPageResult.build(pageResult);
        } catch (Exception ex) {
            logger.error("获取数据失败", ex);
            return EzPageResult.build(0, new ArrayList<City>());
        }
    }
    */

    /**
     * 3.获取指定ID对象的接口
     * URL: /test/ajaxgrid/1 GET
     *
     * @param id
     * @return
     */

    public JsonResult get(@PathVariable("id") ID id) {
        JsonResult result;
        try {

            logger.info("正在查找 id={} 的对象", id);
            T entity = baseService.get(id);
            result = JsonResult.success(entity);
        } catch (Exception ex) {
            String message = "获取数据失败";
            logger.error(message, ex);
            result = JsonResult.error(message);
        }

        return result;
    }

    /**
     * 4.新增接口
     * URL：/test/ajaxgrid/add POST
     * 请求的Body中包含一个业务表实体对象的json数据
     * 由新增窗口的确定按钮触发
     *
     * @param entity
     * @return
     */
    public JsonResult add(@RequestBody T entity) {
        try {
            //一般情况下，我们都是后台产生ID，主键使用GUID
            entity.setId(getNewID());

            logger.info("通过公共方法新增对象");
            baseService.add(entity);

            return JsonResult.success();
        } catch (Exception ex) {
            //写日志
            //做异常处理
            //返回错误信息到前台
            logger.error("新增失败！", ex);
            return JsonResult.error("新增失败！");
        }
    }

    /**
     * 获取一个新的递增或者随机唯一的ID
     *
     * @return
     */
    protected abstract ID getNewID();

    /**
     * 5.更新/修改接口
     * URL：/test/ajaxgrid/update POST
     * 请求的Body中包含一个业务表实体对象的json数据
     * 由编辑窗口的确定按钮触发
     *
     * @param entity
     * @return
     */
    public JsonResult update(@RequestBody T entity) {
        try {
            T entityFromDB = baseService.get(entity.getId());
            if (entityFromDB == null) {
                return JsonResult.error("ID=" + entity.getId() + "不存在！");
            }
            logger.info("通过公共方法更新对象");
            baseService.update(entity);

            return JsonResult.success();
        } catch (Exception ex) {
            //写日志
            //做异常处理
            //返回错误信息到前台
            logger.error("修改失败！", ex);
            return JsonResult.error("修改失败！");
        }
    }


    /**
     * 6.删除接口
     * URL：/test/ajaxgrid/delete/1 POST
     * 请求的Body中无数据，仅在url中包含需要删除的id
     * 由Grid上面工具栏的删除按钮触发
     *
     * @param id
     * @return
     */

    public JsonResult delete(@PathVariable("id") ID id) {
        try {
            T entityFromDB = baseService.get(id);
            if (entityFromDB == null) {
                return JsonResult.error("ID=" + id + "不存在！");
            }

            logger.info("通过公共方法删除对象");

            baseService.delete(id);

            return JsonResult.success();
        } catch (Exception ex) {
            //写日志
            //做异常处理
            //返回错误信息到前台
            logger.error("删除失败！", ex);
            return JsonResult.error("删除失败！");
        }
    }
}
