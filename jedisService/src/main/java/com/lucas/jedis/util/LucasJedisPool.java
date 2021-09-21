package com.lucas.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 基于jedis的连接池
 * @create: 2021-09-12 17:18
 **/
public class LucasJedisPool {
    /**
     * 私有化构造器
     */
    private LucasJedisPool() {
        //Nope
    }

    private final static class JedisPoolInnerHolder {

        /**
         * 私有化构造器
         */
        private JedisPoolInnerHolder() {

        }

        // Redis服务器IP
        private static String ADDR = "192.168.30.77";

        // Redis的端口号
        private static int PORT = 6379;

        // 访问密码
        private static String AUTH = "";

        // 可用连接实例的最大数目，默认值为8；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        private static int MAX_ACTIVE = 1024;

        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        private static int MAX_IDLE = 200;

        // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        private static int MAX_WAIT = 10000;

        private static int TIMEOUT = 10000;

        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        private static boolean TEST_ON_BORROW = true;

        private static redis.clients.jedis.JedisPool jedisPool;

        static {
            try {
                initBasic();
                JedisPoolConfig config = new JedisPoolConfig();
                //config.setMaxActive(MAX_ACTIVE);
                config.setMaxTotal(MAX_ACTIVE);
                config.setMaxIdle(MAX_IDLE);
                //config.setMaxWait(MAX_WAIT);
                config.setMaxWaitMillis(MAX_WAIT);
                config.setTestOnBorrow(TEST_ON_BORROW);
                if (AUTH == null || AUTH.length() == 0) {
                    jedisPool = new redis.clients.jedis.JedisPool(config, ADDR, PORT, TIMEOUT);
                }else{
                    jedisPool = new redis.clients.jedis.JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
                }
            } catch (Exception e) {
                throw new RuntimeException("缓存服务器初始化异常", e);
            }
        }
    }

    /**
     * 初始化配置参数
     */
    private static void initBasic() throws IOException {
        // 从文件中获取redis连接池的基本参数
        Properties props = new Properties();
        InputStream in = LucasJedisPool.class.getResourceAsStream("/redis.properties");
        if(in != null){
            props.load(in);

            String addr = props.getProperty("redis.addr");
            String port = props.getProperty("redis.port");
            String auth = props.getProperty("redis.auth");
            String maxActive = props.getProperty("redis.maxActive");
            String maxIdle = props.getProperty("redis.maxIdle");
            String maxWait = props.getProperty("redis.maxWait");
            String timeout = props.getProperty("redis.timeout");

            if(addr!=null){
                JedisPoolInnerHolder.ADDR = addr;
            }
            if(port!=null){
                JedisPoolInnerHolder.PORT = Integer.parseInt(port);
            }
            if(auth!=null){
                JedisPoolInnerHolder.AUTH = auth;
            }
            if(maxActive!=null){
                JedisPoolInnerHolder.MAX_ACTIVE = Integer.parseInt(maxActive);
            }
            if(maxIdle!=null){
                JedisPoolInnerHolder.MAX_IDLE =Integer.parseInt(maxIdle);
            }
            if(maxWait!=null){
                JedisPoolInnerHolder.MAX_WAIT =Integer.parseInt(maxWait);
            }
            if(timeout!=null){
                JedisPoolInnerHolder.TIMEOUT =Integer.parseInt(timeout);
            }
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public static Jedis getJedisFromJedis() {
        return JedisPoolInnerHolder.jedisPool.getResource();
    }

    /**归还jedis对象*/
    public static void returnJedisOjbect(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }

}
