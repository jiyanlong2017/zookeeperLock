package cn.jyl.client;

import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfiguration{

     @Bean
     public Contract feignContract(){
         return new feign.Contract.Default();
    }

    @Bean
    public FeignBasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }





    /**
     * Feign请求拦截器
     * @author yinjihuan
     * @create 2017-11-10 17:25
     **/
    public class FeignBasicAuthRequestInterceptor  implements RequestInterceptor {

        public FeignBasicAuthRequestInterceptor() {

        }

        @Override
        public void apply(RequestTemplate template) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            template.header("token", request.getHeader("token"));
        }
    }

}

