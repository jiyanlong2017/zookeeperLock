package cn.jyl.controller;

import cn.jyl.model.UserIntegral;
import cn.jyl.service.UserIntegralService;
import com.sinosoft.microservice.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("userintegral")
public class UserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;

    @PostMapping("add/{userId}")
    public JsonResult add(@PathVariable("userId") String userId){

        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setId(UUID.randomUUID().toString());

        userIntegral.setUserid(userId);
        userIntegral.setValue("100");

        userIntegralService.createUserIntegrel(userIntegral);
        return JsonResult.success();
    }

    @PutMapping("decrease/{id}")
    public JsonResult decrease(@PathVariable("id") String userId) {

        boolean flag = userIntegralService.decrease(userId);

        return flag? JsonResult.success(): JsonResult.error("操作失败");

    }
}
