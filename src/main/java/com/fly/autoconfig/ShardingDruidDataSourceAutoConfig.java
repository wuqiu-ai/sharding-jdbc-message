package com.fly.autoconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
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

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("traceId", "ds${traceId % 4}"));
        //orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order${order_id % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());
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
