var ioc={
	 
	ds:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://172.16.1.104/information_schema', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:1050,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	infDao:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"ds"}]
	}
}