package com.anzhi.ssdb;

import org.nutz.ssdb4j.impl.SimpleClient;
import org.nutz.ssdb4j.pool.Pools;

/**
 * 简单封装下，方便配置中构造
 * @author Administrator
 *
 */
public class SSDBPooledClient extends SimpleClient {
	private String name;
	private String host;
	private Integer port;
	 
	public SSDBPooledClient(final String host, final int port, final int timeout, Object cnf, final byte[] auth){
		super( Pools.pool(host, port, timeout, cnf, auth));
		this.host=host;
		this.port=port;
	}
	@Override
	public String toString() {
		return name+"|"+host+":"+port;
	}
	 
}