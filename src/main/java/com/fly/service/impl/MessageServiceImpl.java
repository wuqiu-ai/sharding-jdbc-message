package com.fly.service.impl;

import com.fly.dao.PushMessageMapper;
import com.fly.domain.PushMessage;
import com.fly.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: peijiepang
 * @date 2018/11/14
 * @Description:
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private PushMessageMapper pushMessageMapper;

    @Override
    public PushMessage selectByPrimaryKey(Long id) {
        return pushMessageMapper.selectByPrimaryKey(id);
    }
}
