package com.fly.keygen;

import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: peijiepang
 * @date 2019-01-15
 * @Description:
 */
@Component
public class ShardingKeyGenerator implements KeyGenerator {

    @Autowired
    private com.dxy.keygen.core.KeyGenerator keyGenerator;

    @Override
    public Number generateKey() {
        return keyGenerator.generateKey();
    }
}
