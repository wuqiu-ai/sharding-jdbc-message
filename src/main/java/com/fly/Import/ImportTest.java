package com.fly.Import;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: peijiepang
 * @date 2020/6/8
 * @Description:
 */
public class ImportTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        MyService myService = (MyService)context.getBean("myService");
        System.out.println(myService);
    }
}
