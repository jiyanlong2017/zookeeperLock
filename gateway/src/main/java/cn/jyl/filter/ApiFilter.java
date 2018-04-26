package cn.jyl.filter;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 纪炎龙.
 * 创建时间 2018/1/9.
 */
@Component
public class ApiFilter extends ZuulFilter {

    @Autowired
    RestTemplate restTemplate;

    private String[] ignoreUrl = {
            "uaa/login"
    };

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String requestURI = request.getRequestURI();
        //request.setAttribute("ip", request.getRemoteHost());
        System.out.println("------------------------------调用者IP是" + request.getRemoteHost() + "----");

        for (String url : ignoreUrl) {
            if (requestURI.contains(url)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() {


        RequestContext currentContext = RequestContext.getCurrentContext();
        String token =currentContext.getRequest().getHeader("token");
        if(token == null) {
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(403);

            try {
                HttpServletResponse response = currentContext.getResponse();
                response.getWriter().write("no Permission");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        Boolean flag = verify(token);

        if(flag) {
            currentContext.addZuulRequestHeader("token", token);
            return null;

        } else {
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(403);

            try {
                HttpServletResponse response = currentContext.getResponse();
                response.getWriter().write("no Permission");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @HystrixCommand(groupKey="authGroup", commandKey = "verify"/*,
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),//指定多久超时，单位毫秒。超时进fallback
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//判断熔断的最少请求数，默认是10；只有在一个统计窗口内处理的请求数量达到这个阈值，才会进行熔断与否的判断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),//判断熔断的阈值，默认值50，表示在一个统计窗口内有50%的请求处理失败，会触发熔断
            },
            threadPoolProperties = {

            }*/
    )
    private Boolean verify(String token) {

//        return restTemplate.getForObject("http://auth-service/uaa/verify?token=" + token, Boolean.class);
        return true;

    }
}
