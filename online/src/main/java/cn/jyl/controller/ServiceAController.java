package cn.jyl.controller;

import cn.jyl.client.ServiceBClient;
import cn.jyl.service.BService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping
public class ServiceAController {

    @Value("${name:unknown}")
    private String name;

    @Autowired
    EurekaDiscoveryClient discoveryClient;

    @Autowired
    private ServiceBClient serviceBClient;

    @Autowired
    BService bService;

    @GetMapping(value = "/")
    public String printServiceA() {

        long l = System.currentTimeMillis();
        ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
        String result = serviceInstance.getServiceId() + " (" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + ")" + "===>name:" + name + "<br/>" +
                serviceBClient.printServiceB();

        long l2 = System.currentTimeMillis();

        System.out.println(l2 - l);

        return result;
    }

    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello";
    }


    @GetMapping("queryServiceB")
    public List queryServiceB(){

        return bService.queryBService();
    }



}