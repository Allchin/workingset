package cn.allchin.httpPostRpc;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import com.qunar.flight.nts.commons.service.FuwuPlantformService;

/**
 * 给满足条件类型生成bean定义
 * http://www.tuicool.com/articles/BbAz6v/ ClassPathMapperScanner
 * 
 * @author renxing.zhang
 *
 */
public class HttpPostApiScanner extends ClassPathBeanDefinitionScanner {
	private static Logger logger = LoggerFactory.getLogger(HttpPostApiScanner.class);
	public HttpPostApiScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	/* 
	 * 指定扫描到的类型的接口为mapperInterface,
	 * 扫描到的类型的实际对象由代理生产工厂负责生产,生产的产品角色为mapperInterface
	 * (non-Javadoc)
	 * @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner#doScan(java.lang.String[])
	 */
	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
			logger.warn("xpasDS1|No xmlpost interface was found in pkg|" + Arrays.toString(basePackages));
			return beanDefinitions;
		}
		// if found
		for (BeanDefinitionHolder holder : beanDefinitions) {
			GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
			definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());

			definition.setBeanClass(HttpPostProxyFactoryBean.class);
			
		}
		return beanDefinitions;
	}

	/**
	 * 重写判断方法，是不是我们需要的目标定义
	 * 非常重要
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	}
}
