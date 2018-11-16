package com.fly;

import com.fly.domain.PushMessage;
import com.fly.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private MessageService messageService;

    @Test
    public void test1(){
        PushMessage message1 = messageService.selectByPrimaryKey(12360621643271168L);
//        PushMessage message2 = messageService.selectByPrimaryKey(12360622139247616L);
//        PushMessage message3 = messageService.selectByPrimaryKey(12360621677874176L);
//        PushMessage message4 = messageService.selectByPrimaryKey(12360621674728448L);
        LOGGER.info(".....");
    }

}
