package com.fly.controller;

import com.fly.domain.PushMessage;
import com.fly.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: peijiepang
 * @date 2018/11/15
 * @Description:
 */
@RestController
public class TestController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/test")
    public String test(){
        PushMessage pushMessage1 = messageService.selectByPrimaryKey(1L);
        PushMessage pushMessage2 = messageService.selectByPrimaryKey(2L);
        PushMessage pushMessage3 = messageService.selectByPrimaryKey(3L);
        PushMessage pushMessage4 = messageService.selectByPrimaryKey(4L);
        PushMessage pushMessage5 = messageService.selectByPrimaryKey(5L);
        return "ok";
    }
}
