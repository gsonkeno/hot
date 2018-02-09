package com.gsonkeno.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by gaosong on 2018-02-09
 */
public class RedisClient {
    private static JedisPool pool;
    private int dbNumber;

    public RedisClient (String host, int port, int dbNumber){
        pool = RedisPool.getPool(host, port);
        this.dbNumber = dbNumber;
    }
    public RedisClient (String host, int port){
        pool = RedisPool.getPool(host, port);
    }

    public boolean exists(String key)
    {
        Jedis jedis = null;

        try {
            jedis = pool.getResource();
            jedis.select(dbNumber);
            return jedis.exists(key);
        } catch (Exception e) {
            jedis.close();
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
    }
}
