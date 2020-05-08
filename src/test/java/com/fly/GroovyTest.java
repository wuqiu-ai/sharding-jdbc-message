package com.fly;

import com.google.common.base.Joiner;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.Expando;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * @author: peijiepang
 * @date 2020-02-10
 * @Description:
 */
public class GroovyTest {

    private static final GroovyShell SHELL = new GroovyShell();

    public int doSharing( String algorithmExpression,String columnName, String columnValue) {
        algorithmExpression = Joiner.on("").join("{it -> \"", algorithmExpression, "\"}");
        Script script;
        if (ShardingAlgorithmCache.SCRIPTS.containsKey(algorithmExpression)) {
            script = ShardingAlgorithmCache.SCRIPTS.get(algorithmExpression);
        } else {
            script = SHELL.parse(algorithmExpression);
            ShardingAlgorithmCache.SCRIPTS.put(algorithmExpression, script);
        }
        Closure<?> closure =  (Closure) script.run();
        Closure<?> result = closure.rehydrate(new Expando(), null, null);
        result.setResolveStrategy(Closure.DELEGATE_ONLY);
        result.setProperty(columnName, Long.parseLong(columnValue));
        BigDecimal bigDecimal = new BigDecimal(result.call().toString());
        int index = bigDecimal.setScale( 0, BigDecimal.ROUND_DOWN ).intValue(); // 向下取整
        return index;
    }


    public static void main(String[] args) {
        GroovyTest groovyTest = new GroovyTest();
        while (true){
            Random random = new Random(100000);
            groovyTest.doSharing("${traceid%2}","traceid","7");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
