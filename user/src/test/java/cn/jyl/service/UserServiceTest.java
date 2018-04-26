package cn.jyl.service;

import cn.jyl.UserApplication;
import cn.jyl.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testAdd(){

        User user = new User();
        user.setId("test");
        user.setName("test");
        user.setPassword("password");
        User t = userService.add(user);


    }
}