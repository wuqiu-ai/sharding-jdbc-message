package com.fly.pipeline;

/**
 * pipeline维护了handlercontext链表，对该链表进行增删改操作，同时他对外提供了整个pipeline的调用接口
 * @author: peijiepang
 * @date 2019-05-13
 * @Description:
 */
public class Pipeline {

    /**
     * 链表头
     */
    private HandlerContext head;

    /**
     * 链表尾
     */
    private HandlerContext tail;

    public void work(Object msg){
        head.doWork(msg);
    }

    public void addFirst(Handler handler){
        if(null == head && null ==  tail){
            head = tail = new HandlerContext(handler);
        }else{
            HandlerContext ctx = new HandlerContext(handler);
            HandlerContext tmp = head;
            head = ctx;
            head.setNext(tmp);
        }
    }

}
