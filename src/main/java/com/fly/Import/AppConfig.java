package com.fly.Import;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: peijiepang
 * @date 2020/6/8
 * @Description:
 */
@Configuration
@Import(MyImportBeanDefinitionRegistrar.class)
public class AppConfig {

}
