package cn.allchin.rpc.xmlpost;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qunar.flight.nts.checkin.util.JaxbXmlUtil;

/**
 * 方法代理
 * @author renxing.zhang
 *
 */
public class HttpPostMethod {
	private static Logger logger = LoggerFactory.getLogger(HttpPostMethod.class);
	
	private MethodSign method;

	public HttpPostMethod(Method method)   {
		this.method = new MethodSign(method);
	}
 

	public Object execute(ServiceSession serviceSession, Object[] args) {
		Object result;

		
		Object params =args;
		if(args!=null && args.length ==1){
			params=args[0];//大多数情况下http body 只有一个，我们只需要一个参数
		}
	 
		result= serviceSession.post(params, this.method);
		
		return result;
	}
	
	public MethodSign getMethod() {
		return method;
	}


	public void setMethod(MethodSign method) {
		this.method = method;
	}

	public static class MethodSign {
		private final static ProtocolUtil defaultUtil=new JaxbXmlUtil();
		 
		private boolean returnsVoid; 
	 
		private Class<?> returnType;
		
		private ProtocolUtil serialiseUtil;

		public String url;
		
		private Method method;
		
		
		

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public ProtocolUtil getSerialiseUtil() {
			return serialiseUtil;
		}

		public void setSerialiseUtil(ProtocolUtil serialiseUtil) {
			this.serialiseUtil = serialiseUtil;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
		public MethodSign(Method method)   {
		 
			this.method=method;
			this.returnType = method.getReturnType(); 
			this.returnsVoid = void.class.equals(this.returnType);
			this.url =getApiUrlFromAnnotation(method);//  从方法上面拿注解
			
			//  这个方法的序列化工具
			this.serialiseUtil=getSerializeUtilFromAnnotation(method);
		}

 

 
		private String getApiUrlFromAnnotation(Method method){
			HttpPostCall apiUrl=method.getAnnotation(HttpPostCall.class);
			if(apiUrl!= null){
				String url= apiUrl.url() ;
				return url;
			} 
			return null;
			
		}
		private ProtocolUtil getSerializeUtilFromAnnotation(Method method)  {
			 
			HttpPostSerializeUtil suAnno=method.getAnnotation(HttpPostSerializeUtil.class);
			if(suAnno!= null){
				try {
					return suAnno.value().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					logger.error("获取方法的协议工具异常",e);
				}
			} 
			return defaultUtil;
			
		}
		
 
 
 
 

		public Class<?> getReturnType() {
			return returnType;
		}

		public void setReturnType(Class<?> returnType) {
			this.returnType = returnType;
		}

		public boolean isReturnsVoid() {
			return returnsVoid;
		}

		public void setReturnsVoid(boolean returnsVoid) {
			this.returnsVoid = returnsVoid;
		}
		
		

	}
}
