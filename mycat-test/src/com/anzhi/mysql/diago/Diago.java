package com.anzhi.mysql.diago;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.Sqls;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * mysql性能诊断工具
 * @author zrx
 *
 */
public class Diago {
	private static Ioc ioc ;
	private static NutDao infoDao;
	
	//
	
	public static String table="u_passport_manage";
	public static  long maxRun=3_0000;
	public static void main(String[] args) { 
		 	 
		ioc = new NutIoc(new JsonLoader("diago-datasource.js"));
		//
		DruidDataSource ds=ioc.get(DruidDataSource.class,"ds"); 
		System.out.println(ds.getUrl() );
		//
		infoDao =ioc.get(NutDao.class,"infDao"); 
		//扫描sqls文件夹中的文件，获取诊断算法sql语句
		infoDao.setSqlManager(new FileSqlManager("sqls"));
		//
		int c=infoDao.sqls().count();//keys()之前先调用一次count
		
		
		/*String sqlNames[]={"conn","innoBufferRead","keyCacheRead","keyCacheWrite","lock"
				,"queryCache","queryCacheFragment","queryCacheHit","rwRate","tableScan"
				,"threadCache"};*/
		String sqlNames[]=infoDao.sqls().keys();
		for(String name:sqlNames){
			String conn=infoDao.sqls().get(name); 
			Sql sql=Sqls.create(conn).setCallback(new SqlCallback(){

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql)
						throws SQLException {
					while(rs.next()){
						for(int i=1;i<=Integer.MAX_VALUE;i++){
							try{
								String label=rs.getMetaData().getColumnLabel(i);
								//String colName=rs.getMetaData().getColumnName(i);
								System.out.println(label+"|"+rs.getString(i) +"|");
							}
							catch(Exception e){
								break;
							}
							
						} 
					}
					System.out.println("---------------------");
					return null;
				}
				 
			}) ; 
			infoDao.execute(sql); 
		}
		//QPS
		Qps qps=new Qps(infoDao,"QPS","QUESTIONS","GLOBAL_STATUS");
		Qps tps=new Qps(infoDao,"TPS","Com_commit","GLOBAL_STATUS");
		
		qps.start();
		tps.start();
		try {
			qps.join();
			tps.join();
		} catch (InterruptedException e) { }
		
		System.out.println("");
	}
 
}
/**
 * 监控information_schema表中某个变量的变化量
 * @author Administrator
 *
 */
class Qps extends Thread{
	NutDao dao;
	String label;//指标
	String variableName;
	String tableName;
	//
	Sql sql;
	/**
	 * @param dao 
	 * @param label  指标名称
	 * @param variableName  变量名称
	 * @param tableName  表名
	 */
	public Qps(NutDao dao,final String label,String variableName,String tableName) {
		this.dao=dao;
		this.label=label;
		this.variableName=variableName;
		this.tableName=tableName;
		String sqlString ="select *,unix_timestamp() t from  "+tableName+" where VARIABLE_NAME = '"+variableName+"' ;   ";
		//
		
		sql=Sqls.create(sqlString).setCallback(new SqlCallback(){

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				while(rs!=null && rs.next()){
					 
						try{ 
							Long current=rs.getLong("VARIABLE_VALUE");
							String name=rs.getString( "VARIABLE_NAME");
							Long tdb=rs.getLong("t");
							if(count==null){//第一次
								count=current;
								t=tdb;
							} 
							else{
								double during=tdb-t;
								double c=current-count;
								double qps= (c/during) ;
								
								System.out.println(label+"|"+qps+"|上次查询计数|"+count+"|当前查询计数|"+current+"|VARIABLE_NAME|"+name);
								count=null;
								t=null;
							}
						}
						catch(Exception e){
							break;
						}
						
					 
				}
				 
				return null;
			}
			 
		}) ; 
	}
	
	//计算qps
	Long count;
	Long t;
	//
	int countRange=200;
	 @Override
	public void run() {
				for(int i =0;i<countRange;i++){
					dao.execute(sql);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) { }
				} 	 
	}
}
