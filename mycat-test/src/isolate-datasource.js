var ioc={
	dbDao0:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db0"}]
	}, 
	dbDao1:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db1"}]
	}, 
	dbDao2:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db2"}]
	}, 
	dbDao3:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db3"}]
	}, 
	dbDao4:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db4"}]
	}, 
	dbDao5:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db5"}]
	}, 
	dbDao6:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db6"}]
	}, 
	dbDao7:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db7"}]
	}, 
	dbDao8:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db8"}]
	}, 
	dbDao9:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db9"}]
	}, 
	dbDao10:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db10"}]
	}, 
	dbDao11:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db11"}]
	}, 
	dbDao12:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db12"}]
	}, 
	dbDao13:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db13"}]
	}, 
	dbDao14:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db14"}]
	},
	dbDao15:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db15"}]
	},
	dbDao16:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"db16"}]
	},
	db16:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_16', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db15:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_15', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db14:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_14', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db13:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_13', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db12:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_12', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db11:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_11', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db10:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_10', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db9:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_9', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db8:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_8', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db7:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_7', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db6:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_6', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db5:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_5', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db4:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_4', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db3:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_3', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db2:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_2', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db1:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_1', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	db0:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:3306/user_0', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
				username:'platform',
				password:'platform',
				initialSize:5,
				minIdle:3,
				maxActive:250,
				maxWait:30000,
				timeBetweenEvictionRunsMillis:6000,
				minEvictableIdleTimeMillis:30001,
				poolPreparedStatements:false,
				maxPoolPreparedStatementPerConnectionSize:20, 
				filters:'stat'
		}
	},
	mycat:{
		type:'com.alibaba.druid.pool.DruidDataSource',
		fields:{
				url:'jdbc:mysql://192.168.3.22:8066/platform', //FIXME 如果db连接refused,会有机会重复执行，重复执行时报异常
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
	mycatDao:{
		type:'org.nutz.dao.impl.NutDao',
		args:[{refer:"mycat"}]
	}
}