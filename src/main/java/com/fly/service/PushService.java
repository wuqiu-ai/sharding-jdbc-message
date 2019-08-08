package com.fly.service;

import com.fly.dao.PushMessageMapper;
import com.fly.domain.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: peijiepang
 * @date 2019-07-31
 * @Description:
 */
@Service
public class PushService {

    @Autowired
    private PushMessageMapper pushMessageMapper;

    /**
     * 通过traceid查询push
     * @param traceId
     * @return
     */
    public PushMessage selectByTraceId(Long traceId){
        return pushMessageMapper.selectByTraceId(traceId);
    }

    /**
     * 更新装填
     * @param id
     * @param traceId
     * @return
     */
    @Transactional
    public int updateStatusById(Long id, Long traceId){
        return pushMessageMapper.updateStatusById(id,traceId);
    }
}
