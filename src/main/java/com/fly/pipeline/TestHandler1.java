package com.fly.pipeline;

/**
 * @author: peijiepang
 * @date 2019-05-13
 * @Description:
 */
public class TestHandler1 implements Handler{
    @Override
    public void doHandle(HandlerContext context, Object msg) {
        try {
            System.out.println("do testhandler1 msg:"+msg);
            Thread.sleep(1000);
            context.write(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
