package com.fly.schedule;

import com.fly.mertics.JettyMetrics;
import java.lang.management.ManagementFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: peijiepang
 * @date 2020/7/6
 * @Description:
 */
@Component
public class MertixSchedule {

    private final static Logger LOGGER = LoggerFactory.getLogger(MertixSchedule.class);

    @Autowired
    private JettyMetrics jettyMetrics;

    @Scheduled(cron="0 0/1 * * * ?")
    public void tomcatInfo(){
        ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        LOGGER.info("metrix:"+ jettyMetrics.metrics());
    }

}
