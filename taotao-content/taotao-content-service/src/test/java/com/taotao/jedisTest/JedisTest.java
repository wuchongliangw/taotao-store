package com.taotao.jedisTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.content.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() throws Exception{
		//第一步：创建一个Jedis对象，需要指定服务端的ip及端口
		Jedis jedis = new Jedis("192.168.25.153", 6379,300000);
		//jedis.set("myTest", "123456");
		String result = jedis.get("key1");
		System.out.println(result);
		jedis.close();
	}
	
	//连接单机版使用连接池
	@Test
	public void testJedisPool() throws Exception{
		//第一步：创建一个JedisPool对象，需要指定服务端的ip及端口
		JedisPool jedisPool = new JedisPool("192.168.25.153", 6379);
		//第二步：从JedisPool中获得Jedis对象
		Jedis jedis = jedisPool.getResource();
		//第三步：使用Jedis操作redis服务器
		jedis.set("jedis2", "test");
		String result = jedis.get("jedis");
		System.out.println(result);
		//第四步：操作完毕后关闭jedis对象，连接池回收资源
		jedis.close();
		//第五步：关闭JedisPool对象
		jedisPool.close();
	}
	
	//连接集群版
	@Test
	public void testJedisCluster() throws Exception {
		// 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.153", 7001));
		nodes.add(new HostAndPort("192.168.25.153", 7002));
		nodes.add(new HostAndPort("192.168.25.153", 7003));
		nodes.add(new HostAndPort("192.168.25.153", 7004));
		nodes.add(new HostAndPort("192.168.25.153", 7005));
		nodes.add(new HostAndPort("192.168.25.153", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		// 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
		jedisCluster.set("hello", "100");
		String result = jedisCluster.get("hello");
		// 第三步：打印结果
		System.out.println(result);
		// 第四步：系统关闭前，关闭JedisCluster对象。
		jedisCluster.close();
	}
	
	@Test
	public void testJedisClient() throws Exception{
		ApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("first", "100");
		String result = jedisClient.get("first");
		System.out.println(result);
	}

}
