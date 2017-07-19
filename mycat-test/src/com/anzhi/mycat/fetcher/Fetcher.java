package com.anzhi.mycat.fetcher;

import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

/**
 * Fetcher是用来执行sql查询的，
 * 可以作为压力输入
 * @author Administrator
 *
 */
public class Fetcher {
	private static Ioc ioc ;
	private static NutDao mycat;
	
	//
	
	public static String table="u_passport_manage";
	public static  long maxRun=3_0000;
	public static void main(String[] args) { 
		//
		int tCount=1000;
		if(args!=null && args.length>1){
				try{
					tCount=new Integer(args[0]); 
					maxRun=new Long(args[1]);
					String dsFile=args[2];
					if(dsFile!=null){
						ioc = new NutIoc(new JsonLoader(dsFile));
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
		}
		mycat =ioc.get(NutDao.class,"mycatDao");
		//
		Thread[] ts=new Thread[tCount];
		for(int i=0;i<ts.length;i++){
			
			Thread t=new Thread(){
				@Override
				public void run() { 
					while(true){
						double curr=Math.random()*new Double(maxRun); 
						fetch((long)curr);
						
					}
					 
				}
			};
			ts[i]=t;
			
		}
		//
		for(Thread t:ts){
			t.start(); 
		}
		//
		Thread ticker=new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(10_000);
					} catch (InterruptedException e) {
					 
					}
					System.out.println("heartbeat|"+new Date());
					
				}
			}
			
		};
		ticker.start();
		
	}
	public static boolean fetch(long pid){
		Condition cdt =Cnd.where("pid", "=", pid);
		long dbIdx=(pid-1)/300_0000;
		//物理库读
		NutDao dbDao=ioc.get(NutDao.class,"dbDao"+dbIdx);
		//System.out.println("dbIdx|"+dbIdx+"|daoId|"+dbDao.hashCode());
		Record dbRecord=dbDao.fetch(table,cdt);
	 
		// 
		if(dbRecord!=null){
			//mycat 读
			Record record=mycat.fetch(table, cdt);
			//System.out.println("dbIdx|"+dbIdx+"|daoId|"+mycat.hashCode());
			if(record == null){
				System.out.println("未找到|"+pid);
				return false;
			}
		 
			//物理库读
			record=dbDao.fetch(table, cdt);
			//System.out.println("dbIdx|"+dbIdx+"|daoId|"+mycat.hashCode());
			if(record == null){
				System.out.println("未找到|"+pid);
				return false;
			}
			 
		}
		
		return true; 
	}
	
}
