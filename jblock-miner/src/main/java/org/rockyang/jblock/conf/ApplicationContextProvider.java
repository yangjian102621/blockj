package org.rockyang.jblock.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		context = applicationContext;
	}

	public static void publishEvent(ApplicationEvent event)
	{
		context.publishEvent(event);
	}

	public static Object registerSingletonBean(String beanName, Object singletonObject)
	{

		// convert applicationContext to ConfigurableApplicationContext
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
		//get BeanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();
		// register bean
		defaultListableBeanFactory.registerSingleton(beanName, singletonObject);
		// get registered bean.
		return configurableApplicationContext.getBean(beanName);
	}

	public static ApplicationContext getApplicationContext()
	{
		return context;
	}
}
