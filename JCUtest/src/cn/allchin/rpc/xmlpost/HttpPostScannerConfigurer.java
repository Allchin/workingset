package cn.allchin.rpc.xmlpost;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

/**
 * http post 请求业务初始化工具
 * 
 * @author renxing.zhang
 *
 */
public class HttpPostScannerConfigurer implements BeanDefinitionRegistryPostProcessor  {
	private String basePackages;

 

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// left intentionally blank
 
	}

	/* 
	 * 扫描包，对特定类型注解过的类进行扫描
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		HttpPostApiScanner scanner = new HttpPostApiScanner(registry);

		scanner.addIncludeFilter(new AnnotationTypeFilter(HttpPostApi.class));

		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackages,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

	
	}

	public String getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

 

}
