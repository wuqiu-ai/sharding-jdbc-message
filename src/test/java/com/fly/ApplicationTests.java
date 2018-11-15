package com.fly;

import com.fly.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Autowired
    private MessageService messageService;

    @Test
    public void test1(){
        messageService.selectByPrimaryKey(12294777889360896L);
    }

}
