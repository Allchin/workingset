package com.anzhi.user.kv.check;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.Date;
import java.util.Properties;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 诊断 
 * @author zrx
 *
 */

public class RedisDiago {
	private static Ioc ioc = new NutIoc(new JsonLoader("diago-redis.js")); 
	private static JedisPool pool=ioc.get(JedisPool.class, "pool");
	
	public static void main(String[] args) throws IOException {
		
		
		Jedis jedis = pool.getResource();
		
		String info=jedis.info();
		System.out.println(info);
		pool.returnResource(jedis);
		//
		info=info.replaceAll(":", "=");
		
		
		StringBufferInputStream sbis=new StringBufferInputStream(info);
		Properties prop=new Properties();
		prop.load(sbis);
		//
		 
		Long rdb_last_save_time=1000*new Long(prop.getProperty("rdb_last_save_time"));
		Date date=new Date(rdb_last_save_time);
		System.out.println("上次成功bgsave的时间|"+date+"|rdb_last_save_time");
		//
		Long num=new Long(prop.getProperty("rdb_changes_since_last_save") ); 
		System.out.println("上次bgsave后发生变化的改变数量|"+num+"|rdb_changes_since_last_save");
		//
		Long lastForkUsec=new Long(prop.getProperty("latest_fork_usec"));
		System.out.println("上次fork操作花费时间|ms|"+lastForkUsec+"|latest_fork_usec");
		//命中
		Double keyspace_hits=new Double(prop.getProperty("keyspace_hits"));
		Double keyspace_misses=new Double(prop.getProperty("keyspace_misses"));
		Double rate=keyspace_hits/(keyspace_hits+keyspace_misses);
		System.out.println("keyspace命中率|"+rate);
		//ops
		System.out.println("每秒处理命令数目|"+prop.getProperty("instantaneous_ops_per_sec"));
		
		printKVavgLength(prop, jedis, 8);
	}
	//平均kv长度
	private static Long  printKVavgLength(Properties info,Jedis jedis ,int dbs){
	
				Long mem=new Long(info.getProperty("used_memory"));
				Long dbsize=jedis.dbSize();
				Long len=mem/(dbsize*dbs);
				System.out.println("平均kv大小|"+((len))+"|Bytes");
				return len;
	}
}
