package com.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: peijiepang
 * @date 2019-01-16
 * @Description:
 */
@Configuration
@EnableAutoConfiguration
public class TestAutoConfig {

    @Bean
    public String test(){
        return "Hello";
    }
}
