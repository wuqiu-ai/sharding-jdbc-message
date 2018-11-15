package com.fly.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class Utils {

    private Utils(){}

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final DefaultResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    public static Resource loadResource(String path) {
        return RESOURCE_LOADER.getResource(path);
    }

    public static Resource loadResourceFirst(String path) {
        Resource[] resources = loadResources(path);
        if (resources != null && resources.length > 0) {
            return resources[0];
        }
        return null;
    }

    public static Resource[] loadResources(String path) {
        try {
            return RESOURCE_PATTERN_RESOLVER.getResources(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("无法解析路径path:" + path);
        }
    }


    public static AnnotatedGenericBeanDefinition decorateBeanDefinition(Class clazz) {
        try {
            ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
            AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
            ScopeMetadata metadata = scopeMetadataResolver.resolveScopeMetadata(abd);
            abd.setScope(metadata.getScopeName());
            AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
            return abd;
        } catch (Exception e) {
            log.error("AnnotatedGenericBeanDefinition 添加 class 异常", e);
            throw new IllegalArgumentException("AnnotatedGenericBeanDefinition 添加 class 异常", e);
        }
    }


    public static String doRegisterBean(AnnotatedGenericBeanDefinition abd,
                                        String beanName,
                                        BeanDefinitionRegistry registry) {
        return doRegisterBean(abd, beanName, registry, null);
    }


    public static String doRegisterBean(AnnotatedGenericBeanDefinition abd,
                                        String beanName,
                                        BeanDefinitionRegistry registry,
                                        String[] aliases) {
        try {
            if (StringUtils.isBlank(beanName)) {
                BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
                beanName = beanNameGenerator.generateBeanName(abd, registry);
            }
            BeanDefinitionHolder definitionHolder;
            if (aliases != null && aliases.length > 0) {
                definitionHolder = new BeanDefinitionHolder(abd, beanName, aliases);
            } else {
                definitionHolder = new BeanDefinitionHolder(abd, beanName);
            }
            BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
            return beanName;
        } catch (Exception e) {
            log.error("注册 bean 异常", e);
            throw new IllegalArgumentException("注册 bean 异常", e);
        }
    }


    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Class oo = obj.getClass();
        List<Class> clazzs = new ArrayList<>();
        while (oo != null && oo != Object.class) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
        }
        Map<String, Object> map = new HashMap<>(clazzs.size() * 8);
        try {
            for (Class clazz : clazzs) {
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                }
            }
        } catch (IllegalAccessException e) {
            // ignore
        }
        return map;
    }
}
