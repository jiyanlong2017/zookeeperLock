package cn.jyl.service;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Recipes_Lock {


    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client =
                CuratorFrameworkFactory.newClient("localhost:2181",
                        5000,
                        3000,
                        retryPolicy);
        client.start();


        final CountDownLatch down = new CountDownLatch(1);

        for (int i = 0; i < 300; i++) {

            new Thread(new Runnable() {
                public void run() {

                    try {


                        down.await();
                        String lock_path = "/curator_recipes_lock_path";

                        InterProcessMutex lock = new InterProcessMutex(client, lock_path);

                        if(!lock.acquire(10, TimeUnit.SECONDS)){
                            throw new RuntimeException("获取锁失败");
                        }
//                        lock.acquire();
                        System.out.println("获得锁");
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");

                        String orderNo = sdf.format(new Date());

                        Thread.sleep(1000);

                        System.out.println("生成的订单号是：" + orderNo);
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        }

        down.countDown();
    }

}