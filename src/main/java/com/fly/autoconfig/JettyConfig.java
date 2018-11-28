package com.fly.autoconfig;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jetty自动配置
 * @author: peijiepang
 * @date 2018/11/28
 * @Description:
 */
@Configuration
public class JettyConfig {

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            JettyServerCustomizer jettyServerCustomizer) {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addServerCustomizers(jettyServerCustomizer);
        return factory;
    }


    @Bean
    public JettyServerCustomizer jettyServerCustomizer() {
        return server -> {
            threadPool(server);
            accessLog(server);
        };
    }

    private void threadPool(Server server){
        // Tweak the connection config used by Jetty to handle incoming HTTP
        // connections
        final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
        //默认最大线程连接数200
        threadPool.setMaxThreads(1024);
        //默认最小线程连接数8
        threadPool.setMinThreads(50);
        //默认线程最大空闲时间60000ms
        threadPool.setIdleTimeout(60000);
    }

    //jetty启动日志
    private void accessLog(Server server) {
        NCSARequestLog requestLog = new NCSARequestLog("logs/jetty-yyyy_mm_dd.request.log");
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT+08");
        requestLog.setLogLatency(true);
        requestLog.setRetainDays(60);
        server.setRequestLog(requestLog);
    }

}
