package com.roncoo.eshop.datalink;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTest {

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000 * 10); 
		config.setTestOnBorrow(true);
		JedisPool jedisPool = new JedisPool(config, "192.168.31.223", 1111);
		
		Jedis jedis = jedisPool.getResource();
		
//		jedis.set("k1", "{\"description\":\"测试品牌111\",\"id\":3,\"name\":\"测试品牌111\"}");
		System.out.println(jedis.get("dim_product_1"));    
		
		jedisPool.close();
	}
	
}
