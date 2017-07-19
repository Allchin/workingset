package com.anzhi.ssdb;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.ssdb4j.spi.Response;

/**
 * 核心思想是每秒产生n tps的请求,控制每秒运行上限值
 * @author zhangrenxing@anzhi.com
 * 
 * 
 *
 */
public class LimitSetTest {
	/**
	 * <pre>
	 * ops用来限制你向服务器的压力;
	 * 
	 * 
	 * 虚拟机单机，2cores,ops 测算结果
	 * 大约每个线程一个连接，
	 * 3线程put ops 4610
	 * 4线程 put ops 4721  400w数据;
	 * 8线程put ops 4599
	 * 100线程 put ops 4346
	 * 
	 * 
	 * </pre>
	 */
	private final static int  ops=80_000;
	//
	private static Ioc ioc = new NutIoc(new JsonLoader("limitPush-ssdb.js"));
	private static SSDBPooledClient client=ioc.get(SSDBPooledClient.class, "pool");
	 
	/**
	 *当前运行值 
	 */
	public static AtomicInteger count=new AtomicInteger(0);
	/**
	 * 限制最大计数值，
	 */
	public static AtomicInteger limit=new AtomicInteger(0);
	//结果计数
	public static AtomicInteger success=new AtomicInteger(0);
	public static AtomicInteger fail=new AtomicInteger(0);
	//
	public static ExecutorService tpool=Executors.newFixedThreadPool(1000);
	//
 
	public static void main(String[] args){
		Thread t=new Limiter();
		t.start();
		
		tpool.submit(new ThoundsPutter(true));
		for(int i=0;i<157;i++){
			tpool.submit(new ThoundsPutter(false));
		}
		
		
	}
	/**
	 * 打印最后的记录
	 */
	private static AtomicLong id=new AtomicLong(0);
	public static void  setOne(boolean print){
		try{
			Response resp=client.set("zrx"+id.incrementAndGet()	,UUID.randomUUID().toString());
			if(resp.ok()){
				success.addAndGet(1);
			}
			else{
				fail.addAndGet(1);
			}
		}
		catch(Exception e){
			fail.addAndGet(1);
		}
		if(print){
			System.out.println("setOne|run|"+count.get()+"|limit|"+limit.get()+"|ok|"+success.get()+"|fail|"+fail.get()+"|real-ops|"+(success.get()/(limit.get()/ops)));
		}
		
	}
	/**
	 *TODO  1000次操作证使用同一个长连接
	 * @author Administrator
	 *
	 */
	public static class ThoundsPutter implements Runnable{
		private  boolean print=false;
		public ThoundsPutter(boolean p) {
			 print=p;
		}

		@Override
		public void run() {
			while(true){
				for(int i=0;i<ops;i++){//充分利用单线程
					if(count.get()<limit.get()){
						int c=count.getAndAdd(1);
						if(c<limit.get()){
							setOne(print);
						}
					} 
				}
			}
			 
		}
		 
		
	}
	
	
	 
	//每秒补充一次Limit ,控制每秒ops
	public static  class Limiter extends Thread{
		@Override
		public void run() {
			while(true){
				limit.addAndGet(ops);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { }
				
			} 
		}
	}
	
}
