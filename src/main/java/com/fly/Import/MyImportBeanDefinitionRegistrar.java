package com.fly.Import;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: peijiepang
 * @date 2020/6/8
 * @Description:
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
        MyClassPathBeanDefinitionScanner scanner = new MyClassPathBeanDefinitionScanner(registry, false);
        scanner.registerFilters();
        scanner.doScan("com.fly.Import");
    }
}
