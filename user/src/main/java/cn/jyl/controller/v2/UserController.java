package cn.jyl.controller.v2;

import cn.jyl.model.User;
import cn.jyl.service.UserService;
import com.sinosoft.microservice.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController(value = "UserControllerV2")
@RequestMapping("/v2/user")
public class UserController extends cn.jyl.controller.v1.UserController {

    @Autowired
    protected UserService userService;

    /**
     * 第二个版本， 解决分布式事务问题
     * @param userId
     * @param password
     * @return
     */
    @Override
    public JsonResult updateUser(@PathVariable("id") String userId, @PathVariable("password") String password) {

        return userService.updateUser(userId, password);
    }
}
