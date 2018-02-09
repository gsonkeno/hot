package com.gsonkeno.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by gaosong on 2018-02-09
 */
public class RedisPool {
    private static JedisPool pool;
    public static JedisPool getPool(String host, int port){
        if (pool == null){
            JedisPoolConfig poolConfig = new JedisPoolConfig();

            poolConfig.setMaxIdle(10);

            poolConfig.setMaxTotal(100);

            poolConfig.setMaxWaitMillis(10000);

            poolConfig.setTestOnBorrow(true);

            pool = new JedisPool(poolConfig,host,port);
        }
        return pool;
    }
}
