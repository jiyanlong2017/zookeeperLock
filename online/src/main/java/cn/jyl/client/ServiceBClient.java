package cn.jyl.client;

import feign.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = "svcb-service",
        configuration = FeignConfiguration.class)
public interface ServiceBClient {

    @RequestLine("GET /")
    String printServiceB();

    @Component
    class ServiceBClientFallback implements ServiceBClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBClientFallback.class);

        @Override
        public String printServiceB() {
            LOGGER.info("异常发生，进入fallback方法");
            return "SERVICE B FAILED! - FALLING BACK";
        }
    }
}