package cn.allchin.httpPostRpc.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.util.PropertyPlaceholderHelper;

import cn.allchin.httpPostRpc.ServiceSession;

/**
 * 代理者
 * 
 * 代理访问的具体执行类
 * 
 * @author renxing.zhang
 *
 * @param <T>
 */
public class HttpPostProxy<T> implements InvocationHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7562540157310391613L;
	private ServiceSession serviceSession; 
	private Map<Method, HttpPostMethod> methodCache;
	//
	private List<Properties> properties;

	private static PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper(
			PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX,
			PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX,
			PlaceholderConfigurerSupport.DEFAULT_VALUE_SEPARATOR, false);

	/**
	 * @param serviceSession 需要代理者访问的服务器会话
	  
	 * @param methodCache 访问服务器的方法的缓存池，加速用，避免每次生成一个方法对象
	 */
	public HttpPostProxy(ServiceSession serviceSession,  
			Map<Method, HttpPostMethod> methodCache) {
		this.serviceSession = serviceSession;
	 
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}

		final HttpPostMethod mapperMethod = cachedMapperMethod(method);
		return mapperMethod.execute(serviceSession, args);

	}

	private HttpPostMethod cachedMapperMethod(Method method) {
		HttpPostMethod mapperMethod = methodCache.get(method);
		if (mapperMethod == null) {
			mapperMethod = new HttpPostMethod(method);
			methodCache.put(method, mapperMethod);
			
			//
			if(properties != null){
				for(Properties pps:properties){
					String url = mapperMethod.getMethod().getUrl();
					url =strictHelper.replacePlaceholders( url, pps);
					if(StringUtils.isNotEmpty(url)){
						mapperMethod.getMethod().setUrl(url);
					}
					
				}  
			}
		
			
		}
		return mapperMethod;
	}

	public ServiceSession getServiceSession() {
		return serviceSession;
	}

	public void setServiceSession(ServiceSession serviceSession) {
		this.serviceSession = serviceSession;
	}

 

	public Map<Method, HttpPostMethod> getMethodCache() {
		return methodCache;
	}

	public void setMethodCache(Map<Method, HttpPostMethod> methodCache) {
		this.methodCache = methodCache;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}
	

}
