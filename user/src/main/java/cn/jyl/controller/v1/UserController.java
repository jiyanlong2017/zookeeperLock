package cn.jyl.controller.v1;

import cn.jyl.model.User;
import cn.jyl.service.UserService;
import com.sinosoft.microservice.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController(value = "UserControllerV1")
@RequestMapping("/v1/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("addUser")
    public User addUser(@RequestBody User user){
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        return userService.createUser(user);

    }

    @PutMapping("updateUser/{id}/{password}")
    public JsonResult updateUser(@PathVariable("id") String userId, @PathVariable("password") String password) {

        return userService.updateUser(userId, password);
    }

}
