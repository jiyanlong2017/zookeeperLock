package cn.jyl.service;

import com.distributed.lock.Callback;
import com.distributed.lock.zk.ZkDistributedLockTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class LockTest {


    public static void main(String[] args) throws Exception {

        //创建zookeeper的客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181", retryPolicy);
        client.start();

        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");

        mutex.acquire();

        //获得了锁, 进行业务流程
//        doSomething();

        //完成业务流程, 释放锁
        mutex.release();

        //关闭客户端
        client.close();

    }
}
