package com.fly;

import com.fly.dao.AppDeviceMapper;
import com.fly.dao.PushMessageMapper;
import com.fly.domain.AppDevice;
import com.fly.domain.PushMessage;
import com.fly.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private PushMessageMapper pushMessageMapper;

    @Autowired
    private AppDeviceMapper appDeviceMapper;

    @Test
    public void messageTest(){
        PushMessage message1 = pushMessageMapper.selectByPrimaryKey(12360621643271168L);
//        PushMessage message2 = messageService.selectByPrimaryKey(12360622139247616L);
//        PushMessage message3 = messageService.selectByPrimaryKey(12360621677874176L);
//        PushMessage message4 = messageService.selectByPrimaryKey(12360621674728448L);
        LOGGER.info(".....");
    }

    @Test
    public void saveMessage(){
        PushMessage pushMessage = new PushMessage();
        pushMessage.setJobid("333");
        pushMessage.setTraceid(1L);
        pushMessage.setDeviceplatform("asasa");
        pushMessage.setDevicetoken("asas");
        pushMessage.setStatus("11");
        pushMessage.setUserid("33");
        pushMessage.setAppversion("asasa");
        pushMessage.setOsversion("asa");
        pushMessage.setSendtime(new Date());
        pushMessage.setClicktime(new Date());
        pushMessage.setCreatetime(new Date());
        pushMessage.setModifytime(new Date());
        pushMessageMapper.insertSelective(pushMessage);
        LOGGER.info(".....");
    }

    @Test
    public void appDeviceTest(){
        AppDevice appDevice = appDeviceMapper.selectByPrimaryKey(200);
        Assert.assertFalse(null != appDevice);
    }

    /**
     * 测试没有分片的sql直接经过ss，distince语法（默认分片不支持）
     */
    @Test
    public void distinceTest(){
//        List<AppDevice> appDevices = appDeviceMapper.selectTestDistince();
//        Assert.assertFalse(null != appDevices);
    }

    @Test
    public void iptest() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String hostName = address.getHostName();
        Long workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
    }

}
