package cn.allchin.httpPostRpc.spring.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Component;

/**
 * 获取properties文件中的内容
 * @author renxing.zhang
 *
 */
@Component
public class PlaceHolderUtil implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	private static Logger logger = LoggerFactory.getLogger(PlaceHolderUtil.class);
	
	public Properties getRuntimeProperties() {
		PropertyResourceConfigurer prc = applicationContext.getBean(PropertyResourceConfigurer.class);

		Properties pps = PlaceHolderUtil.getPlaceholderProperties(prc);
		
		return pps;
	}
	

	public static List<Properties>  getPlaceholderProperties(Map<String, PropertyResourceConfigurer>  placeHolderMap){
		if(placeHolderMap==null || placeHolderMap.size() ==0){
			return null;
		}
		List<Properties> result=new ArrayList<Properties>();
		for(PropertyResourceConfigurer ppc:placeHolderMap.values()){
			Properties pps =getPlaceholderProperties(ppc) ;
			result.add(pps);
		}
		
		return result;
	
	}
 
	public static Properties getPlaceholderProperties(PropertyResourceConfigurer propertyResourceConfigurer) {
		Properties properties = new Properties();
		try {
			// get the method mergeProperties
			// in class PropertiesLoaderSupport
			Method mergeProperties = PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");
			// get the props
			mergeProperties.setAccessible(true);
			Properties props = (Properties) mergeProperties.invoke(propertyResourceConfigurer);

			// get the method convertProperties
			// in class PropertyResourceConfigurer
			Method convertProperties;

			convertProperties = PropertyResourceConfigurer.class.getDeclaredMethod("convertProperties",
					Properties.class);

			// convert properties
			convertProperties.setAccessible(true);
			convertProperties.invoke(propertyResourceConfigurer, props);
			
			properties.putAll(props);
		} catch ( Exception e) {
			logger.error("placeHodlery读取异常",e);
		}
		
		return properties;
	}
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		
	}

 
}
