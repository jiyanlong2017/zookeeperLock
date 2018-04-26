package cn.jyl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("lock")
public class TestLockController {


    private final String USER = "http://user";

    @Autowired
    private RestTemplate restTemplate;


    //调用无锁接口
    @GetMapping("testWithOutLock")
    public void testWithOutLock(){

        restTemplate.getForObject(USER + "/v3/user/updateMoney/006f473f-d164-4515-af76-4b3541e1dcaa", Object.class);
    }


    //调用有锁接口
    @GetMapping("testWithLock")
    public void testWithLock(){


        restTemplate.getForObject(USER + "/v3/user/updateMoneyWithLock/006f473f-d164-4515-af76-4b3541e1dcaa", Object.class);

    }

}
