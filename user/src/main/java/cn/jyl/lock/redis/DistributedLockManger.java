package cn.jyl.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;

/**
 * Redis分布式锁 单节点模式
 */
@Component
public class DistributedLockManger {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockManger.class);

    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 60000;

    //    private static final int DEFAULT_BATCH_EXPIRE_TIME = 6;

    @Autowired
    private Jedis jedis;


    private static DistributedLockManger lockManger;

    public DistributedLockManger() {
    }

    @PostConstruct
    public void init() {
        lockManger = this;
        lockManger.jedis = this.jedis;
    }

    /**
     * 获取锁 如果锁可用   立即返回true，  否则立即返回false，作为非阻塞式锁使用
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean tryLock(String key, String value) {
        try {
            return tryLock(key, value, 0L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false，作为阻塞式锁使用
     *
     * @param key                            锁键
     * @param value                          被谁锁定
     * @param timeout                        尝试获取锁时长，建议传递500,结合实践单位，则可表示500毫秒
     * @param unit，建议传递TimeUnit.MILLISECONDS
     * @return
     * @throws InterruptedException
     */
    public static  boolean tryLock(String key, String value, long timeout) throws InterruptedException {
        //纳秒
        long begin = System.currentTimeMillis();
        do {
            //LOGGER.debug("{}尝试获得{}的锁.", value, key);

            Long i = lockManger.jedis.setnx(key, value);
            if (i == 1) {
                int singlExpireTime = 0;
                if(timeout == 0){
                    singlExpireTime = DEFAULT_SINGLE_EXPIRE_TIME;
                } else {
                    singlExpireTime = Math.toIntExact(timeout/1000);
                }
                lockManger.jedis.expire(key,singlExpireTime);
                logger.info("{}成功获取{}的锁,设置锁过期时间为{}秒 ", value, key, singlExpireTime);
                return true;
            } else {
                // 存在锁 ，但可能获取不到，原因是获取的一刹那间
                String desc = lockManger.jedis.get(key);
               // logger.debug("{}正被{}锁定.", key, desc);
            }
            if (timeout == 0) {
                break;
            }
            //logger.info("注释:{}获取{}锁失败,设置锁过期时间为{}秒,需重新获取,但需要休眠100毫秒", value, key, unit.toNanos(timeout));
            //在其睡眠的期间，锁可能被解，也可能又被他人占用，但会尝试继续获取锁直到指定的时间
            Thread.sleep(10);
        }
        while ((System.currentTimeMillis() - begin) < timeout);
        logger.info("获取{}锁失败,原因:超时",key);
        //因超时没有获得锁
        return false;
    }

    /**
     * 释放单个锁
     *
     * @param key   锁键
     * @param value 被谁释放
     */
    public static void unLock(String key, String value) {
        try {
            lockManger.jedis.del(key);
            logger.debug("{}锁被{}释放 .", key, value);
        } catch (JedisConnectionException je) {
            logger.debug("{}锁被{}释放失败.{}", key, value,je);
        } catch (Exception e) {
            logger.debug("{}锁被{}释放失败.{}", key, value,e);
        }
    }
}
