package cn.jyl.controller.v3;

import cn.jyl.model.User;
import cn.jyl.lock.redis.DistributedLockManger;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v3/user")
public class UserController extends cn.jyl.controller.v2.UserController {

    @Autowired
    CuratorFramework client;


    @GetMapping("updateMoney/{id}")
    public User updateMoney(@PathVariable("id") String userId) {


        User user = userService.get(userId);

        //获取钱
        int money = Integer.parseInt(user.getMoney());

        //减1
        user.setMoney(String.valueOf(--money));

        //更新
        userService.update(user);

        return user;

    }


    @GetMapping("updateMoneyWithLock/{id}")
    public User updateMoneyWithLock(@PathVariable("id") String userId) {

        try {
            //获取锁对象
            String lock_path = "/curator_recipes_lock_path";
            InterProcessMutex lock = new InterProcessMutex(client, lock_path);


            if(!lock.acquire(10, TimeUnit.SECONDS)){
                throw new RuntimeException("获取锁失败");
            }

            User user = userService.get(userId);

            System.out.println("获取user对象成功");
            //获取钱
            int money = Integer.parseInt(user.getMoney());

            //减1
            user.setMoney(String.valueOf(--money));
            System.out.println("money-1");

            //更新
            userService.update(user);
            System.out.println("更新user对象成功");
            //释放锁
            lock.release();

            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }


}
