package com.fly;

import com.dxy.keygen.utils.OrderNoGeneratorUtils;
import com.fly.dao.AppDeviceMapper;
import com.fly.domain.AppDevice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: peijiepang
 * @date 2018/11/14
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
public class ApplicationTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationTests.class);

    @Autowired
    private AppDeviceMapper appDeviceMapper;

    @Test
    public void appDeviceTest(){
        AppDevice appDevice = appDeviceMapper.selectByPrimaryKey(200);
        Assert.assertFalse(null != appDevice);
    }

    @Test
    public void iptest() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String hostName = address.getHostName();
        Long workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
    }

}
