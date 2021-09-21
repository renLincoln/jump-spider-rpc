package com.lucas.jedis.util;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: redis工具类
 * @create: 2021-09-12 17:39
 **/
public class RedisUtil {
    /**
     * 获取hash表中所有key
     * @param name
     * @return
     */
    public static Set<String> getHashAllKey(String name){
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.hkeys(name);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 从redis hash表中获取
     * @param hashName
     * @param key
     * @return
     */
    public static String getHashKV(String hashName,String key){
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.hget(hashName, key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 删除hash表的键值对
     * @param hashName
     * @param key
     */
    public static Long delHashKV(String hashName,String key){
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.hdel(hashName,key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 存放hash表键值对
     * @param hashName
     * @param key
     * @param value
     */
    public static Long setHashKV(String hashName,String key,String value){
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.hset(hashName,key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 删除键值对
     * @param k
     * @return
     */
    public static Long delKV(String k){
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.del(k);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 放键值对
     * 永久
     * @param k
     * @param v
     */
    public static String setKV(String k, String v)
    {
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.set(k, v);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }


    /**
     * 放键值对
     *
     * @param k
     * @param v
     */
    public static String setKV(String k,int second, String v)
    {
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.setex(k,second, v);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 根据key取value
     *
     * @param k
     * @return
     */
    public static String getKV(String k)
    {
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.get(k);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }


    /**
     * 存放set
     *
     * @param k
     * @return
     */
    public static Long sadd(String k,String ...members)
    {
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.sadd(k,members);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 随机获取set集合中的成员
     *
     * @param k
     * @return
     */
    public static String srandmember(String k)
    {
        Jedis jedis = null;
        try {
            jedis = LucasJedisPool.getJedisFromJedis();
            return jedis.srandmember(k);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LucasJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }
}
