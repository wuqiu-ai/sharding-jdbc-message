package com.fly.algorithm;

import com.alibaba.fastjson.JSON;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author: peijiepang
 * @date 2018-12-06
 * @Description:
 */
public class MyPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyPreciseShardingAlgorithm.class);


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        LOGGER.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));
        Long mod = shardingValue.getValue() % availableTargetNames.size();
        List<String> result = new ArrayList<>(availableTargetNames.size());
        availableTargetNames.stream().forEach(t->{
            result.add(t);
        });
        return result.get(mod.intValue()).toString();
    }
}
