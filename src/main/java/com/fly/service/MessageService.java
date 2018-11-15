package com.fly.service;

import com.fly.domain.PushMessage;

/**
 * @author: peijiepang
 * @date 2018/11/14
 * @Description:
 */
public interface MessageService {

    PushMessage selectByPrimaryKey(Long id);

}
