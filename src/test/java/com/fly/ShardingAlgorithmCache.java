package com.fly;

import groovy.lang.Script;
import java.util.HashMap;
import java.util.Map;

/**
 * 分片策略缓存
 * @author: peijiepang
 * @date 2019-03-13
 * @Description:
 */
public class ShardingAlgorithmCache {

    /**
     * groovy分片算法缓存
     */
    public static final Map<String, Script> SCRIPTS = new HashMap<String,Script>();

}
