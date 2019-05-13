package com.fly.pipeline;

/**
 * @author: peijiepang
 * @date 2019-05-13
 * @Description:
 */
public class TestPipeline {
    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();
        pipeline.addFirst(new TestHandler2());
        pipeline.addFirst(new TestHandler1());
        for(int i=0;i<10;i++){
            pipeline.work("test"+i);
        }
    }
}
