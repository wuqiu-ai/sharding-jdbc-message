package com.fly.autoconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.orchestration.config.OrchestrationConfiguration;
import io.shardingsphere.orchestration.reg.api.RegistryCenterConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.orchestration.api.OrchestrationShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: peijiepang
 * @date 2018/11/14
 * @Description:
 */
@Slf4j
@Configuration
@EnableAutoConfiguration
@MapperScan("com.fly.dao")
public class ShardingDruidDataSourceAutoConfig{

    @Primary
    @Bean(name = "mainDataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties(prefix = "dxy.datasource.main-data-source")
    public DruidDataSource mainDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "ds0DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds0")
    public DruidDataSource ds0DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "ds1DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds1")
    public DruidDataSource ds1DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "ds2DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds2")
    public DruidDataSource ds2DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "ds3DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds3")
    public DruidDataSource ds3DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 分库分表-模式
     * @return
     * @throws SQLException
     */
    @Bean(name = "dataSource",autowire = Autowire.BY_NAME)
    public DataSource dataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("main",mainDataSource());
        dataSourceMap.put("ds0",ds0DataSource());
        dataSourceMap.put("ds1",ds1DataSource());
        dataSourceMap.put("ds2",ds2DataSource());
        dataSourceMap.put("ds3",ds3DataSource());

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("push_message");
        orderTableRuleConfig.setActualDataNodes("ds${0..3}.push_message");

        DefaultKeyGenerator defaultKeyGenerator = new DefaultKeyGenerator();
        defaultKeyGenerator.setWorkerId(21L);
        orderTableRuleConfig.setKeyGenerator(defaultKeyGenerator);
        orderTableRuleConfig.setKeyGeneratorColumnName("id");

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("traceId", "ds${traceId % 4}"));
        //orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order${order_id % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        //默认数据源
        shardingRuleConfig.setDefaultDataSourceName("main");
        // 获取数据源对象
//        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());

        //数据治理功能
        // 配置注册中心
        RegistryCenterConfiguration regConfig = new RegistryCenterConfiguration();
        regConfig.setServerLists("zk1.host.dxy:2181,zk2.host.dxy:2181,zk3.host.dxy:2181");
        regConfig.setNamespace("sharding-sphere-orchestration");
        // 配置数据治理
        OrchestrationConfiguration orchConfig = new OrchestrationConfiguration("orchestration-sharding-data-source", regConfig, false);

        Properties properties = new Properties();
        properties.setProperty("sql.show","true");

        DataSource dataSource = OrchestrationShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties, orchConfig);


//        DataSource dataSource = OrchestrationShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties(), orchConfig);
        return dataSource;
    }

    /**
     * 读写分离模式
     * @return
     * @throws SQLException
     */
    @Bean(name = "dataSource1",autowire = Autowire.BY_NAME)
    public DataSource dataSource1() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("main",mainDataSource());
        dataSourceMap.put("ds0",ds0DataSource());
        dataSourceMap.put("ds1",ds1DataSource());
        dataSourceMap.put("ds2",ds2DataSource());
        dataSourceMap.put("ds3",ds3DataSource());

        // 配置分库 + 分表策略
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration("test","main",
                Arrays.asList("ds0","ds1","ds2","ds3"));
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,masterSlaveRuleConfiguration,
                new HashMap<>(),new Properties());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws SQLException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean;
    }

//    @Bean(name = "mapperScannerConfigurer")
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setBasePackage("com.fly.mapper");
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        return mapperScannerConfigurer;
//    }
}
