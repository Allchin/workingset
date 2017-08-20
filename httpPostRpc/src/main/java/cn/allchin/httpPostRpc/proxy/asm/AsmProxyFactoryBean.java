package cn.allchin.httpPostRpc.proxy.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.allchin.httpPostRpc.ServiceSession;
import cn.allchin.httpPostRpc.annotation.domain.ServiceDomain;
import cn.allchin.httpPostRpc.proxy.HttpPostMethod;
import cn.allchin.httpPostRpc.proxy.HttpPostProxy;
import cn.allchin.httpPostRpc.spring.util.PlaceHolderUtil;

/**
 * 代理访问bean的制造工厂
 * @author renxing.zhang
 *
 * @param <T>
 */
@ServiceDomain
public class AsmProxyFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
	private Class<T> mapperInterface;
	private Map<Method, HttpPostMethod> methodCache = new HashMap<Method, HttpPostMethod>();
	private ServiceSession ss = new ServiceSession();
	//
	private ApplicationContext applicationContext;

	public T newInstance() {
		 
		Map<String, PropertyResourceConfigurer>  placeHolderMap=applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		List<Properties> pps = PlaceHolderUtil.getPlaceholderProperties(placeHolderMap);
 

		HttpPostProxy<T> mapperProxy = new HttpPostProxy<T>(ss, methodCache);
		mapperProxy.setProperties(pps);
		return newInstance(mapperProxy);
	}

	@SuppressWarnings("unchecked")
	public T newInstance(HttpPostProxy<T> mapperProxy) {
		
		
		//TODO asm create instance like AsmPostProxy
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface },
				mapperProxy);
	}

	@Override
	public T getObject() throws Exception {
		return newInstance();
	}

	@Override
	public Class<?> getObjectType() {
		return mapperInterface;
	}

	@Override
	public boolean isSingleton() {

		return true;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public Map<Method, HttpPostMethod> getMethodCache() {
		return methodCache;
	}

	public void setMethodCache(Map<Method, HttpPostMethod> methodCache) {
		this.methodCache = methodCache;
	}

	public ServiceSession getSs() {
		return ss;
	}

	public void setSs(ServiceSession ss) {
		this.ss = ss;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
