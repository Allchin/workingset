package cn.allchin.httpPostRpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

/**
 * http post ����ҵ���ʼ������
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
	 * ɨ��������ض�����ע����������ɨ��
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
