package cn.jyl.lock.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 王涛 on 2017/7/16.
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * #jedisPoolConfig
     redis.maxIdle=2000
     redis.maxActive=5000
     redis.maxWait=30000
     redis.testOnBorrow=true
     #jedisCluster
     redis.connectionTimeout=10000
     redis.maxAttempts=3
     redis.soTimeout=2000
     */
    private int    expireSeconds;
    private String hosts;
    private int    connectionTimeout;
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }



    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "expireSeconds=" + expireSeconds +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}