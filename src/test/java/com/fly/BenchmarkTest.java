package com.fly;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: peijiepang
 * @date 2018-12-12
 * @Description:
 */
public class BenchmarkTest {


    @State(Scope.Benchmark)
    public static class SpringTest{
        private final static Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.SpringTest.class);

        private ConfigurableApplicationContext context;
        private JdbcTemplate jdbcTemplate = null;
        private String sql = "select * from push_message where traceId = 1 limit 1";

        @Setup
        public void init() {
            String args = "";
            context = SpringApplication.run(ShardingApplication.class, args );
            jdbcTemplate = (JdbcTemplate)context.getBean("fourJdbcTemplate");
            LOGGER.info("初始化完成");
        }

        @Benchmark
        @BenchmarkMode({Mode.Throughput,Mode.AverageTime})
        public void orderNoGeneratorBenchmarkTest() {
            jdbcTemplate.queryForList(sql);
        }
    }

    @Test
    public void benchmarkTest() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("SpringTest")
                .warmupIterations(5)//预热做5轮
                .measurementIterations(10)//正式计量测试做10轮
                .forks(3)//做3轮测试
                .build();
        new Runner(opt).run();
    }
}
