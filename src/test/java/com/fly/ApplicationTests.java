package com.fly;

import com.fly.dao.AppDeviceMapper;
import com.fly.dao.PushMessageMapper;
import com.fly.domain.AppDevice;
import com.fly.domain.PushMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
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

    @Autowired
    private PushMessageMapper pushMessageMapper;

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

    @Test
    public void testInsert(){
        PushMessage pushMessage = new PushMessage();
        pushMessage.setTraceid(212121L);
        pushMessage.setAppversion("asdas");
        pushMessage.setDeviceplatform("asdad");
        pushMessage.setDevicetoken("asdadssa");
        pushMessage.setJobid("asdada");
        pushMessage.setOsversion("asdada");
        pushMessage.setSendtime(new Date());
        pushMessage.setStatus("asda");
        pushMessage.setUserid("asdadasda");
        pushMessage.setCreatetime(new Date());
        pushMessage.setClicktime(new Date());
        pushMessage.setSendtime(new Date());
        pushMessage.setModifytime(new Date());
        pushMessageMapper.insert(pushMessage);
    }
}
