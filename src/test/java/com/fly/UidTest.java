package com.fly;

import com.baidu.fsg.uid.UidGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: peijiepang
 * @date 2018-12-14
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
public class UidTest {

    @Resource
    private UidGenerator uidGenerator;

    @Test
    public void uidTest(){
        uidGenerator.getUID();
    }
}
