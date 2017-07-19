package com.anzhi.mycat.test;

import java.util.concurrent.atomic.AtomicLong;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.log.Logs;

/**
 * 当时mycat 1.2 事务隔离有问题，
 * 这个程序模拟一定量的压力，
 * 进行mycat 升级1.3.0.3后的事务隔离
 * @author zhangrenxing@anzhi.com
 *
 */
public class IsolationTest {
	private static Ioc ioc ;
	private static NutDao mycat;
	
	//
	public static AtomicLong globalPid=new AtomicLong(1);
	public static String u_passport_manage_copy="u_passport_manage_copy";
	public static  long maxRun=3_0000;
	public static void main(String[] args) { 
		//
		int tCount=1000;
		if(args!=null && args.length>1){
				try{
					tCount=new Integer(args[0]); 
					System.out.println("线程|"+tCount);
					maxRun=new Long(args[1]);
					System.out.println("最大pid|"+maxRun);
					
					String dsFile=args[2];
					if(dsFile!=null){
						ioc = new NutIoc(new JsonLoader(dsFile));
					}
					System.out.println("配置|"+dsFile);
					
					Long start =new Long(args[3]);
					globalPid=new AtomicLong(start);
					System.out.println("启动点|"+start);
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
					while(globalPid.get()<maxRun){
						long curr=globalPid.getAndIncrement(); 
						if(curr<maxRun){
							isEqual(curr);
						}
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
				while(globalPid.get()<maxRun){
					try {
						Thread.sleep(10_000);
					} catch (InterruptedException e) {
					 
					}
					System.out.println("current|"+globalPid.get());
					
				}
			}
			
		};
		ticker.start();
		
	}
	public static boolean isEqual(long pid){
		Condition cdt =Cnd.where("pid", "=", pid);
		long dbIdx=(pid-1)/300_0000;
		//物理库读
		NutDao dbDao=ioc.get(NutDao.class,"dbDao"+dbIdx);
		//System.out.println("dbIdx|"+dbIdx+"|daoId|"+dbDao.hashCode());
		Record dbRecord=dbDao.fetch(u_passport_manage_copy,cdt);
		// 修改字段
		String ran=Math.random()+"";
		
		Chain chain=Chain.make("memo", ran);
		//mycat 写
		mycat.update(u_passport_manage_copy, chain, cdt);
		// 
		if(dbRecord!=null){
			//mycat 读
			Record record=mycat.fetch(u_passport_manage_copy, cdt);
			//System.out.println("dbIdx|"+dbIdx+"|daoId|"+mycat.hashCode());
			if(record == null){
				System.out.println("未找到|"+pid);
				return false;
			}
			//  字段不匹配
			if(!ran.equals( record.getString("memo"))){
				System.out.println("mycat|read|修改的字段,值不同|"+pid+"|mycat获取数据|"+record.getString("memo")+"|期望|"+ran);
				return false;
			}
			//物理库读
			record=dbDao.fetch(u_passport_manage_copy, cdt);
			//System.out.println("dbIdx|"+dbIdx+"|daoId|"+mycat.hashCode());
			if(record == null){
				System.out.println("未找到|"+pid);
				return false;
			}
			//  字段不匹配
			if(!ran.equals( record.getString("memo"))){
				System.out.println("physicDB|修改的字段,值不同|"+pid+"|物理db获取|"+record.getString("memo")+"|期望|"+ran);
				return false;
			}
		}
		
		return true; 
	}
	
}
