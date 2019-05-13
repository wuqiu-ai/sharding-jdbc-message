package com.fly.pipeline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 上下文
 * @author: peijiepang
 * @date 2019-05-13
 * @Description:
 */
public class HandlerContext {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Handler handler;

    /**
     * 下一个引用
     */
    private HandlerContext next;

    public HandlerContext(Handler handler) {
        this.handler = handler;
    }

    public void setNext(HandlerContext ctx){
        this.next = ctx;
    }

    /**
     * 执行任务的时候向线程池提交一个runnable的任务，任务中调用handler
     * @param msg
     */
    public void doWork(Object msg){
        if(handler == null){
            return;
        }else{
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //考虑到最后一个节点是空节点
//                    if(null != handler){
                        //把下一个handler的context穿个handler来实现回调
                        handler.doHandle(next,msg);
//                    }
                }
            });
        }
    }

    /**
     * 这里的write操作是给handler调用的，实际上是一个回调方法，当handler处理完数据之后，调用一下nextcontext.write，此时就把任务传递给下一个handler了。
     * @param msg
     */
    public void write(Object msg){
        doWork(msg);
    }

}
