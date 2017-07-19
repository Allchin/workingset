package com.anzhi.user.kv.check;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.anzhi.user.kv.check.Check.IssureRecord;

/**
 * 随机一个key，检查kv缓存是否正确
 * @author Administrator
 *
 */
public class RandomCheck extends Thread{
	private File f=null;
	public static Map<String ,IssureRecord   > uidNotExistMap=new ConcurrentHashMap<String, Check.IssureRecord>();
	public static Thread t=new Thread(){
		public void run() {
			while(true){
				System.out.println("issusMap|size|"+uidNotExistMap.size());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) { }
			}
		};
	};
	private static Logger telLogger =Logger.getLogger("redisClearLog");
	private static Logger uidLogger =Logger.getLogger("redisClearUidLog");
	private static Logger emailLogger =Logger.getLogger("redisClearEmailLog");
	
 
	
	private NutDao dao;
	private JedisPool pool;
	public RandomCheck(NutDao dao, JedisPool pool ,String fileName) {
		this.dao=dao;
		this.pool=pool;
		f=new File(fileName);
	}
 
	/**
	 * @param dao
	 * @param pool
	 * @param key kv的key
	 * @param val pid
	 * @param db
	 */
	public static void handleOneRecord(NutDao dao, JedisPool pool,String key,String val,int db){
		Jedis redis=null;
		try{
			redis=pool.getResource(); 
			redis.select(db); 
			//
			String segs[]=key.split(":");
			String last=segs[segs.length-1];
			//
			if(key.startsWith("u:tel")){
				 
				Record rcd=dao.fetch("u_passport_manage", Cnd.where("telephone", "=", last));
				if(rcd==null){
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|数据库无此tel|"+last);
					IssureRecord ir =new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_EXIST);
					//  查询账户是否在缓存里存在
					{
						String pidKey="u:pid:"+val;
						int idx=Check.selectDb(redis, key, dao);
						redis.select(idx);
						boolean existPassport=redis.exists(pidKey);
						if(existPassport){
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_EXIST;
						}
						else{
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_NOT_EXIST;
						}
					} 
					telLogger.debug( ir);
					//redis.del(key);
					
					return ;
					  
				}
				String pid=rcd.getString("pid");
				if(!val.equals(pid)){
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|与数据库PID不同|"+pid);
					IssureRecord ir = new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_SYNC) ;
					telLogger.debug(ir);
					//redis.del(key);
					return ;
				 
				}
 
			}
			else
			if(key.startsWith("u:uid")){
				Record rcd=dao.fetch("u_passport_manage", Cnd.where("uid", "=", last));
				if(rcd==null){
					IssureRecord ir =new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_EXIST) ;
				//  查询账户是否在缓存里存在
					{
						String pidKey="u:pid:"+val;
						int idx=Check.selectDb(redis, key, dao);
						redis.select(idx);
						boolean existPassport=redis.exists(pidKey);
						if(existPassport){
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_EXIST;
						}
						else{
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_NOT_EXIST;
						}
					} 
					uidLogger.debug(ir);
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|数据库无此uid|"+last);
					//redis.del(key);
					return ;
				 
				}
				String pid=rcd.getString("pid");
				if(!val.equals(pid)){
					IssureRecord ir = new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_SYNC) ;
					uidLogger.debug(ir);
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|与数据库PID不同|"+pid);
					//redis.del(key); 
					return ;
				} 
			}
			else
			if(key.startsWith("u:e")){
				Record rcd=dao.fetch("u_passport_manage", Cnd.where("email", "=", last));
				if(rcd==null){
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|数据库无此email|"+last);
					IssureRecord ir =  new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_EXIST) ;
					// 查询账户是否在缓存里存在
					{
						String pidKey="u:pid:"+val;
						int idx=Check.selectDb(redis, key, dao);
						redis.select(idx);
						boolean existPassport=redis.exists(pidKey);
						if(existPassport){
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_EXIST;
						}
						else{
							ir.memo+=IssureRecord.MEMO_PASSPORT_CACHE_NOT_EXIST;
						}
					} 
					emailLogger.debug(ir);
					
					//redis.del(key);
					return ;
				 
				}
				String pid=rcd.getString("pid");
				if(!val.equals(pid)){
					IssureRecord ir =  new IssureRecord(db, new Long(val), last,IssureRecord.MEMO_NOT_SYNC) ;
					emailLogger.debug(ir);
					System.out.println("db|"+db+"|key|"+key+"|val|"+val+"|与数据库PID不同|"+pid);
					//redis.del(key);
					return ;
				 
				} 
			}
			
		}
		catch(Exception  e){
			e.printStackTrace();
		}
		finally{
			if(redis!=null){
				pool.returnResource(redis);
			} 
		}
	}

	@Override
	public void run() {
		//13336928#0#u:tel:18975578858
		
		try( RandomAccessFile raf=new RandomAccessFile(f,"r") ) {
			String str = raf.readLine();
			while(str!=null){
				//"13336928#0#u:tel:18975578858"
				 
				try{
					String segs[]=str.split("#");
					String key=segs[2];
					String val=segs[0];
					int db=new Integer(segs[1]);
				 
					// 
					handleOneRecord(dao, pool, key, val, db);
				}
				catch(Exception e){e.printStackTrace();}
				
				str = raf.readLine();
			}
			System.exit(0);
		} catch (NumberFormatException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		
		
	
		 
	}
	public void runAll() {
		 
			Jedis redis=null;
			try{
				redis=pool.getResource();
				
				
				for(int db=0;db<8;db++){
					redis.select(db);
					
					//
					String[] keysLike={"u:uid*","u:e*","u:tel*" };
					for(String like:keysLike){
						System.out.println("current|"+like+"|db|"+db);
						Set<String> keys=redis.keys(like);
						String ks[]=new String[1];
						ks=keys.toArray(ks);
						keys=null;
						List<String> vals=redis.mget(ks);
						
						//
						for(int i=0;i<ks.length;i++){
							String key=ks[i];
							String val=vals.get(i);
							//
							String segs[]=key.split(":");
							String last=segs[segs.length-1];
							// 
							handleOneRecord(dao, pool, key, val, db);
							if(i%1000==0){
								System.out.println("during|"+i+"|size|"+ks.length);
							}
						} 
					}
				} 
				
			}
			catch(Exception  e){
				e.printStackTrace();
			}
			finally{
				if(redis!=null){
					pool.returnResource(redis);
				} 
			}
		 
	}
}