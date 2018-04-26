package cn.jyl.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

/**
 * @author jiyanlong
 * @date 2018/1/9
 * 网关统一异常处理
 */
@Component
public class ErrorHandlerFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerFilter.class);


    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return requestContext.getThrowable() != null;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletResponse response = ctx.getResponse();
        PrintWriter writer = null;

        int status = response.getStatus();
        try {
            writer = response.getWriter();
            if (status == 429) {
                response.setContentType("application/json; charset=utf-8");
//            Jsonresult jsonresult = new Jsonresult();
//            jsonresult.setCode(429);
//            jsonresult.setMsg("系统繁忙，稍后重试");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error", 429);
                jsonObject.put("msg", "系统繁忙,稍后重试");
                writer.print(jsonObject);
                writer.flush();
                writer.close();
            }

        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return null;
    }

}
