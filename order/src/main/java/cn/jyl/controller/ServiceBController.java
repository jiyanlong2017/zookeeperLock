package cn.jyl.controller;

import brave.Tracing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RefreshScope
@RestController
public class ServiceBController {

    @Autowired
    EurekaDiscoveryClient discoveryClient;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${msg:unknown}")
    private String msg;

    @GetMapping(value = "/")
    public String printServiceB() {

        String sql = "insert into test (name) values('test')";

        jdbcTemplate.execute(sql);

//        Random random = new Random();
//        int i = random.nextInt(10000);
//        System.out.println(i);
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
        return serviceInstance.getServiceId() + " (" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + ")" + "===>Say " + msg;
    }


    @GetMapping("query")
    @Transactional
    public List query(){

        String sql = "select * from test";

        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);

        return map;
    }

}