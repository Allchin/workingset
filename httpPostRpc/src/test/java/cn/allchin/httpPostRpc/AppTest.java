package cn.allchin.httpPostRpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.allchin.httpPostRpc.proxy.HttpPostMethod;
import cn.allchin.httpPostRpc.proxy.HttpPostProxy;
import cn.allchin.httpPostRpc.proxy.HttpPostProxyFactoryBean;

/**
 * Unit test for simple App.
 */
public class AppTest   { 
	@Test
	public void testVisit() throws Exception {
		HttpPostProxyFactoryBean<VoidReturnServcie> factory=new HttpPostProxyFactoryBean<VoidReturnServcie>();
		factory.setMapperInterface(VoidReturnServcie.class);
		ServiceSession ss=new ServiceSession();
		
		Map<Method, HttpPostMethod> methodCache=new HashMap<>();
		HttpPostProxy<VoidReturnServcie> mapperProxy = new HttpPostProxy<VoidReturnServcie>(ss, methodCache);
		mapperProxy.setProperties(null);
		
		VoidReturnServcie service=factory.newInstance(mapperProxy);
		
		String result = service.visit("acdc");
		
		System.out.println(result);
		
		
		  
	}
	
	public static void main(String[] args) throws Exception {
		new AppTest().testVisit();
	}
	
}
