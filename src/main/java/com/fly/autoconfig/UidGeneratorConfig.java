//package com.fly.autoconfig;
//
//import com.baidu.fsg.uid.impl.DefaultUidGenerator;
//import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 分布式主键生成器
// * @author: peijiepang
// * @date 2018-12-14
// * @Description:
// */
//@Slf4j
//@Configuration
//@EnableAutoConfiguration
//public class UidGeneratorConfig {
//
//    /**
//     * 用完即弃的WorkerIdAssigner，依赖DB操作
//     */
//    @Bean("disposableWorkerIdAssigner")
//    public DisposableWorkerIdAssigner disposableWorkerIdAssigner(){
//        return new DisposableWorkerIdAssigner();
//    }
//
//    /**
//     * 默认生成器
//     * @param disposableWorkerIdAssigner
//     */
//    @Bean("defaultUidGenerator")
//    public DefaultUidGenerator defaultUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner){
//        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
//        defaultUidGenerator.setTimeBits(29);
//        defaultUidGenerator.setWorkerBits(21);
//        defaultUidGenerator.setSeqBits(13);
//        defaultUidGenerator.setEpochStr("2018-12-01");
//        defaultUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
//        return defaultUidGenerator;
//    }
//
//}
