package com.fly;

import com.fly.dao.AppDeviceMapper;
import com.fly.domain.AppDevice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    /**
     * 正常sql测试
     */
    @Test
    public void appDeviceTest(){
        AppDevice appDevice = appDeviceMapper.selectByPrimaryKey(200);
        Assert.assertFalse(null != appDevice);
    }

    /**
     * testDistinct 测试
     */
    @Test
    public void testDistinct(){
        List<AppDevice> list = appDeviceMapper.selectTestDistince();
        Assert.assertFalse(null != list);
    }

    /**
     * testUnion 测试
     */
    @Test
    public void testUnion(){
        List<AppDevice> list = appDeviceMapper.selectTestUnion();
        Assert.assertFalse(null == list);
    }

    @Test
    public void selectTestScheme(){
        List<AppDevice> list = appDeviceMapper.selectTestScheme();
        Assert.assertFalse(null != list);
    }
}
