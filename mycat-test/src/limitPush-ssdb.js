var ioc={
	 
	pool:{
		type:'com.anzhi.ssdb.SSDBPooledClient',
		args:["192.168.4.106",8888,20000,{refer:"config"},""]
	},
	 
	config:{
		type:'redis.clients.jedis.JedisPoolConfig',
		fields:{ 
			maxActive:2000,
			maxIdle:3,
			maxWait:60000,
			testOnBorrow:false,
			testOnReturn:false
		} 
	} 
}