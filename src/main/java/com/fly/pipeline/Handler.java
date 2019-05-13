package com.fly.pipeline;

/**
 * 该接口只需要实现字符串处理，然后把结果传给下一个handler就可以了
 * @author: peijiepang
 * @date 2019-05-13
 * @Description:
 */
public interface Handler {
    void doHandle(HandlerContext context,Object msg);
}
