package cn.jyl.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

/**
 * Created by 王涛 on 2017/7/16.
 */
@Configuration
public class JedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Autowired
    private RedisProperties redisProperties;

    private static JedisPool pool;


    public static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(1000*10);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            pool = new JedisPool(config, "localhost", 6379, 10000);

        }
        return pool;
    }
    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     * @return
     */
    @Bean
    public Jedis getJedis() {
        if (pool == null) {
            pool = getPool();
        }
        return pool.getResource();
    }
}
