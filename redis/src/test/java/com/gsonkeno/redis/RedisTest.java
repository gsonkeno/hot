package com.gsonkeno.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by gaosong on 2018-02-08
 */
public class RedisTest {
    public static void main(String[] args) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(10);

        poolConfig.setMaxTotal(100);

        poolConfig.setMaxWaitMillis(10000);

        poolConfig.setTestOnBorrow(true);

        JedisPool jedisPool = new JedisPool(poolConfig,"127.0.0.1",6379);
        Jedis jedis = jedisPool.getResource();

        System.out.println(jedis.getDB());

        jedisPool.close();
        jedis.close();
    }

    @Test
    public void test(){
        RedisClient redisClient = new RedisClient("127.0.0.1", 6379,2);
        boolean name = redisClient.exists("name");
        System.out.println(name);
    }
}
