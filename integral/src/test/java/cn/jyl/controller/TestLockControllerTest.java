package cn.jyl.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestLockControllerTest {

    @Autowired
    TestLockController testLockController;

    @Test
    public void testWithOutLock() throws Exception {


        ExecutorService e = Executors.newFixedThreadPool(100);
        CountDownLatch down = new CountDownLatch(1);

        Runnable runnable = new Runnable(){
            @Override
            public void run() {

                try {
                    down.await();
                    testLockController.testWithOutLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        for (int i = 0; i < 1000; i++) {
            try {
                e.execute(runnable);
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }

        down.countDown();
        Thread.sleep(1000000);
    }

    @Test
    public void testWithLock() throws Exception {


        ExecutorService e = Executors.newFixedThreadPool(100);
        CountDownLatch down = new CountDownLatch(1);

        Runnable runnable = new Runnable(){
            @Override
            public void run() {

                try {
                    down.await();
                    testLockController.testWithLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };


        for (int i = 0; i < 1000; i++) {
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