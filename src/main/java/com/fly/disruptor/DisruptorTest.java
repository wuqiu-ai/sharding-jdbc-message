//package com.fly.disruptor;
//
//import com.lmax.disruptor.EventFactory;
//import com.lmax.disruptor.EventHandler;
//import com.lmax.disruptor.EventTranslatorOneArg;
//import com.lmax.disruptor.RingBuffer;
//import com.lmax.disruptor.dsl.Disruptor;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
///**
// * @author: peijiepang
// * @date 2019-01-02
// * @Description:
// */
//public class DisruptorTest {
//
//    //声明disruptor中事件类型及对应的事件工厂
//    private class LongEvent {
//        private long value;
//
//        public long getValue() {
//            return value;
//        }
//
//        public void setValue(long value) {
//            this.value = value;
//        }
//    }
//
//    private EventFactory<LongEvent> eventFactory = new EventFactory<LongEvent>() {
//        public LongEvent newInstance() {
//            return new LongEvent();
//        }
//    };
//
//    //声明disruptor，
//    private int ringBufferSize = 1024;
//    private Executor executor = Executors.newFixedThreadPool(8);
//    private static Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor);
//
//
//    //pubisher逻辑，将原始数据转换为event，publish到ringbuffer
//    private class Publisher implements EventTranslatorOneArg<LongEvent , String> {
//
//        public void translateTo(LongEvent event, long sequence, String arg0) {
//            event.setValue(Long.parseLong(arg0));
//        }
//    }
//
//    //consumer逻辑，获取event进行处理
//    private class Consumer implements EventHandler<LongEvent> {
//        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
//            long value = event.getValue();
//            int index = (int) (value % Const.NUM_OF_FILE);
//            fileWriter[index].write("" + value + "\n");
//
//            if(value == Long.MAX_VALUE) {
//                isFinish = true;
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        //注册consumer启动disruptor
//        disruptor.handleEventsWith(new Consumer());
//        disruptor.start();
// 
//        //获取disruptor的ringbuffer，用于生产数据
//        private RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
//        ringBuffer.publishEvent(new Publisher(), line);
//    }
//
//
//
//
//
//}
