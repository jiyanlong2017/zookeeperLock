package cn.jyl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
    public class HelloController {

        /**
         * 测试hello
         * @return
         */
        @RequestMapping(value = "/hello",method = RequestMethod.GET)
        public String hello(Model model) {
            model.addAttribute("name", "Dear");
            return "login";
        }

    }