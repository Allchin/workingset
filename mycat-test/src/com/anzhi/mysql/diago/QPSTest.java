package com.anzhi.mysql.diago;

import java.util.concurrent.atomic.AtomicLong;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

/**
 * mysql qps性能诊断工具;
 * 服务器做查询能达到的qps,
 * 一些基本调优方向:
 * 线程数目用公式算出来
 * testonborrow和testonreturn关掉
 * @author zrx
 *
 */
public class QPSTest {
	private static Ioc ioc ;
	private static NutDao infoDao;
	
	//
	static int tCount=8;
	public static String table="information_schema";
	//
	private static AtomicLong count=new AtomicLong(0);
 
	public static void main(String[] args) { 
		 	 
		ioc = new NutIoc(new JsonLoader("qps-datasource.js")); 
		infoDao =ioc.get(NutDao.class,"infDao");
		//
		Thread qpsT=new Thread(){
			public void run() {
				while(true){
					
					long current=count.get();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) { }
					long last=count.get();
					System.out.println(last-current+"|qps");
				}
			};
		};
		qpsT.start();
		//
		Thread[] ts=new Thread[tCount];
		for(int i=0;i<ts.length;i++){
			
			Thread t=new Thread(){
				@Override
				public void run() { 
					while(true){
						infoDao.fetch("global_variables", Cnd.limit());
						count.incrementAndGet();
					}
					 
				}
			};
			ts[i]=t;
			
		}
		//
		for(Thread t:ts){
			t.start();
		}	
	 
		
		System.out.println("");
	}
 
}
 
