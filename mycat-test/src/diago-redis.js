var ioc={
	config:{
		type:'redis.clients.jedis.JedisPoolConfig',
		fields:{ 
			maxActive:3000,
			maxIdle:3,
			maxWait:1000,
			testOnBorrow:false,
			testOnReturn:false
		} 
	},
	pool:{
		type:'redis.clients.jedis.JedisPool',
		args:[{refer:"config"},"192.168.3.5",6379,2000,"platform"]
	} 
	
}