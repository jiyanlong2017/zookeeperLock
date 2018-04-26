package cn.jyl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 纪炎龙.
 * 创建时间 2018/1/9.
 */
@Controller
@RequestMapping
public class DemoController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
