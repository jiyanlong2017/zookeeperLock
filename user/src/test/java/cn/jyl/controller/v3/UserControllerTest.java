package cn.jyl.controller.v3;

import cn.jyl.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    UserController userController;


    //不加锁
    @Test
    public void MoneyByUserId() throws Exception {

        ExecutorService e = Executors.newFixedThreadPool(100);
        CountDownLatch down = new CountDownLatch(1);

        Runnable runnable = new Runnable(){
            @Override
            public void run() {

                try {
                    down.await();

                    User user = userController.updateMoney("006f473f-d164-4515-af76-4b3541e1dcaa");
                    String money = user.getMoney();
                    System.out.println(money);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };


        for (int i = 0; i < 100; i++) {
            try {
                e.execute(runnable);

            } catch (Exception e1){
                 e1.printStackTrace();
            }

        }

        down.countDown();
        Thread.sleep(1000000);

    }


    //加锁
    @Test
    public void updateMoneyWithLock() throws Exception {

        CountDownLatch down = new CountDownLatch(1);
        ExecutorService e = Executors.newFixedThreadPool(100);
        Runnable runnable = new Runnable(){
            @Override
            public void run() {

                try {
                    down.await();

                    User user = userController.updateMoneyWithLock("006f473f-d164-4515-af76-4b3541e1dcaa");
                    String money = user.getMoney();
                    System.out.println(money);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        for (int i = 0; i < 99; i++) {
            try {
                e.execute(runnable);

            } catch (Exception e1){
                e1.printStackTrace();
            }

        }

        down.countDown();


        Thread.sleep(1000000);
    }

}