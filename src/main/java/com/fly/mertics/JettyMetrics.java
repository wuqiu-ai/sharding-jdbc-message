package com.fly.mertics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: peijiepang
 * @date 2020/7/6
 * @Description:
 */
@Component
public class JettyMetrics implements PublicMetrics, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Collection<Metric<?>> metrics() {
        if (this.applicationContext instanceof EmbeddedWebApplicationContext) {
            EmbeddedServletContainer embeddedServletContainer = ((EmbeddedWebApplicationContext)applicationContext)
                .getEmbeddedServletContainer();
            if (embeddedServletContainer instanceof JettyEmbeddedServletContainer) {
                MonitoredQueuedThreadPool executor = (MonitoredQueuedThreadPool) ((JettyEmbeddedServletContainer) embeddedServletContainer).getServer().getThreadPool();
                //register tomcat thread pool stat
                List<Metric<?>> metrics = new ArrayList<Metric<?>>();
                metrics.add(new Metric<Integer>("the maximum number of busy threads",executor.getMaxBusyThreads()));
                return metrics;
            }
        }
        return Collections.emptySet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
