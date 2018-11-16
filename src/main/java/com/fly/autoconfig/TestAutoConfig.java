package com.fly.autoconfig;

import com.fly.domain.PushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 测试BeanDefinitionRegistryPostProcessor动态注入bean
 * @author: peijiepang
 * @date 2018/11/16
 * @Description:
 */
@Configuration
public class TestAutoConfig implements BeanDefinitionRegistryPostProcessor,
        ApplicationContextAware,EnvironmentAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestAutoConfig.class);

    private ApplicationContext applicationContext = null;

    private Environment environment = null;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 注册Bean定义，容器根据定义返回bean
        LOGGER.info("postProcessBeanDefinitionRegistry register personManager1>>>>>>>>>>>>>>>>");
        //构造bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(PushMessage.class);
        BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder
                .getRawBeanDefinition();
        //注册bean定义
        registry.registerBeanDefinition("personManager1", personManagerBeanDefinition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //do something
        LOGGER.info("postProcessBeanFactory...");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public PushMessage pushMessage(){
        LOGGER.info("pushMessage....");
        PushMessage pushMessage = (PushMessage)this.applicationContext.getBean("personManager1");
        LOGGER.info("pushMessage:{}",pushMessage);
        return pushMessage;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
