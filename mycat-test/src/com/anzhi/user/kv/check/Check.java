package com.anzhi.user.kv.check;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.druid.util.StringUtils;

/**
 * kv缓存，db一致性检查
 * @author zhangrenxing@anzhi.com
 *
 */
public class Check {
	private static Ioc ioc = new NutIoc(new JsonLoader("kv-redis.js")); 
	private static JedisPool pool=ioc.get(JedisPool.class, "pool");
	public static JedisPool omPool=ioc.get(JedisPool.class, "omPool");
	public static final   int  tc=100;
	//
	public static ExecutorService tpool=Executors.newFixedThreadPool(tc);
	//
	public static int selectDb(Jedis redis,String key,NutDao dao){
		String uidDbSql="select abs(CRC32('$key'))%8";
		Sql sql=Sqls.create(uidDbSql); 
		sql.vars().set("key", key);
		 
		//System.out.println("sql|"+sql);
		sql.setCallback(new SqlCallback(){

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				 while(rs.next()){
					 return rs.getInt(1);
				 }
				return null;
			}
			
		}); 
		dao.execute(sql); 
		int dbIdx=sql.getInt();
		return dbIdx;
	}
	//
	public static String getValueFromCache(Jedis redis,String key,NutDao dao){
	 
		int dbIdx=selectDb(redis, key, dao);
		 
		redis.select(dbIdx);
		return redis.get(key);
	}
	public static void isSync(long pid,Jedis redis,NutDao dao ){
		Record record=dao.fetch("u_passport_manage", Cnd.where("pid", "=", pid));
		if(record==null){ 
			return ;
		}
		//
		String dbemail=record.getString("email");
		
		{
			if(!StringUtils.isEmpty(dbemail)){
				String redisPid=getValueFromCache(redis, "u:e:"+dbemail, dao);
				if(!StringUtils.isEmpty(redisPid)  &&
						!redisPid.equals(pid+"")){
					System.out.println("用户|db pid|"+pid+"|db cache不一致|cache|"+"u:e:"+dbemail+"|redisPid|"+redisPid);
				}
			}
		}
		
		//
		String dbUid=record.getString("uid");
		{
			if(!StringUtils.isEmpty(dbUid)){
				String redisPid=getValueFromCache(redis, "u:uid:"+dbUid, dao);
				if(!StringUtils.isEmpty(redisPid)  &&
						!redisPid.equals(pid+"")){
					System.out.println("用户|db pid|"+pid+"|db cache不一致|cache|"+"u:uid:"+dbUid+"|redisPid|"+redisPid);
				}
			}
		}
		
		//
		String dbTel=record.getString("telephone");
		{
			if(!StringUtils.isEmpty(dbTel)){
				String redisPid=getValueFromCache(redis, "u:tel:"+dbTel, dao);
				if(!StringUtils.isEmpty(redisPid)  &&
						!redisPid.equals(pid+"")){ 
					System.out.println("用户|db pid|"+pid+"|db cache不一致|cache|"+"u:tel:"+dbTel+"|redisPid|"+redisPid);
				}
			}
		}
		
		
		
	}
	public static class IssureRecord{
		public static String MEMO_PASSPORT_CACHE_EXIST="|pce";
		public static String MEMO_PASSPORT_CACHE_NOT_EXIST="|npce";
		public static String MEMO_NOT_EXIST="ne";
		public static String MEMO_NOT_SYNC="ns";
		public IssureRecord(int dbi,long pid,String k,String memo) {
			 dbIdx=dbi;
			 this.pid=pid;
			 key=k;
			 this.memo=memo;
		}
		public int dbIdx;
		public Long pid;
		public String key;
		public String memo;
		@Override
		public String toString() {
			return "|"+pid+"|"+key+"|"+dbIdx+"|"+memo;
		}
	}
	 
	//
	
	
	
	public static void fileCheck(String fileName){
		JedisPool pool=ioc.get(JedisPool.class, "pool"); 
		NutDao dao =ioc.get(NutDao.class,"infDao"); 
		tpool.execute(new RandomCheck(dao,pool,fileName));
		//RandomCheck.t.start();
		
		
	}
	public static void mannalCheck(){

		JedisPool pool=ioc.get(JedisPool.class, "pool");
		Jedis redis=pool.getResource();
		NutDao dao =ioc.get(NutDao.class,"infDao"); 
		try{ 
			//以cache维度出发，判断pid是否正确
			long pid =42010028; 
			String uid="20150618171937s69F9mA9Yn"; 
			Record record=dao.fetch("u_passport_manage", Cnd.where("pid", "=", pid));
			System.out.println(record.getString("login_name"));
			//
			String tel="13350805665";
			String pidFromTelInCache =getValueFromCache(redis,"u:tel:"+tel,dao);
			System.out.println("kv缓存中电话对应的pid|tel|"+tel+"|pid|"+pidFromTelInCache);
			String dbTel=record.getString("telephone");
			System.out.println("db中pid对应的db电话|"+pid+"|tel|"+dbTel);
			String telsPidInDb=null;
			try{
				telsPidInDb=dao.fetch("u_passport_manage",Cnd.where("telephone","=",tel)).getString("pid");
					
			}
			catch(Exception  e){}
			System.out.println("缓存电话对应的db pid|"+telsPidInDb);
			
			
			// 
		
			String pidFromUidInCache =getValueFromCache(redis,"u:uid:"+uid,dao);
			System.out.println("kv缓存中uid对应的pid|"+uid+"|pid|"+pidFromUidInCache);
			String dbuid=record.getString("uid");
			System.out.println("db中pid对应的dbuid|"+pid+"|uid|"+dbuid);
			//
			 
			String email="";
			String pidFromEmailInCache =getValueFromCache(redis,"u:e:"+email,dao);
			System.out.println("email|kv cache|"+email+"|pid|"+pidFromEmailInCache);
			String dbe=record.getString("email");
			System.out.println("db|pid|"+pid+"|email|"+dbe);
		}
		catch(Exception  e){
			e.printStackTrace();
		}
		finally{
			pool.returnResource(redis);
		}
		 
	}
	public static void main(String[] args) { 
		//mannalCheck();
		//fromDbSideCheck 
		fileCheck(args[0]);//new File("/home/mysql/fromRedis/vk_u:e.txt");
		
	}
	
	public static void fromDbSideCheck(){
		final JedisPool pool=ioc.get(JedisPool.class, "pool");
				
				final NutDao dao =ioc.get(NutDao.class,"infDao"); 
				try{ 
					
					final AtomicLong pid=new AtomicLong(1);
					
					class DoCheck extends Thread{
						public void run() {
							
							Jedis redis=pool.getResource();
							while(pid.get()<1000_0000){
								
								try { 
									isSync(pid.incrementAndGet(), redis, dao);
								} catch (Exception e) {
									e.printStackTrace();
								}
								finally{
									
								}
								 	 
							}
							pool.returnResource(redis);
						};
					};
					class Ticker extends Thread{
						@Override
						public void run() {
							while(true){
								System.out.println(pid.get());
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) { }
							}
						}
					}
					new Ticker().start();
					for(int i=0;i<tc;i++){
						Thread t=new DoCheck();
						t.start();
					}
					
				}
				catch(Exception  e){
					e.printStackTrace();
				}
				finally{
					
				}
	}
}
