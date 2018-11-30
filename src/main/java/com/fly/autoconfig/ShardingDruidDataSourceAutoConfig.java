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
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
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
import org.springframework.jdbc.core.JdbcTemplate;

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
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return commonConfigurete(dataSource);
    }

    @Bean(name = "ds0DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds0")
    public DruidDataSource ds0DataSource(){
        return commonConfigurete(DruidDataSourceBuilder.create().build());
    }

    @Bean(name = "ds1DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds1")
    public DruidDataSource ds1DataSource(){
        return commonConfigurete(DruidDataSourceBuilder.create().build());
    }

    @Bean(name = "ds2DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds2")
    public DruidDataSource ds2DataSource(){
        return commonConfigurete(DruidDataSourceBuilder.create().build());
    }

    @Bean(name = "ds3DataSource",autowire = Autowire.BY_NAME)
    @ConfigurationProperties("dxy.datasource.ds3")
    public DruidDataSource ds3DataSource(){
        return commonConfigurete(DruidDataSourceBuilder.create().build());
    }

    /**
     * 通用数据源配置
     * @param dataSource
     * @return
     */
    public DruidDataSource commonConfigurete(DruidDataSource dataSource){
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(200);
        dataSource.setMaxWait(10000);//配置从连接池获取连接等待超时的时间
        //配置间隔多久启动一次DestroyThread，对连接池内的连接才进行一次检测，
        // 单位是毫秒。检测时:1.如果连接空闲并且超过minIdle以外的连接，如果空闲时间超过minEvictableIdleTimeMillis设置的值则直接物理关闭。2.在minIdle以内的不处理。
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        //配置一个连接在池中最大空闲时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(30000);
        //设置从连接池获取连接时是否检查连接有效性，true时，如果连接空闲时间超过minEvictableIdleTimeMillis进行检查，否则不检查;false时，不检查
        dataSource.setTestWhileIdle(true);
        //检验连接是否有效的查询语句。如果数据库Driver支持ping()方法，则优先使用ping()方法进行检查，否则使用validationQuery查询进行检查。(Oracle jdbc Driver目前不支持ping方法)
        dataSource.setValidationQuery("select 1 from dual");
        //打开后，增强timeBetweenEvictionRunsMillis的周期性连接检查，minIdle内的空闲连接，每次检查强制验证连接有效性. 参考：https://github.com/alibaba/druid/wiki/KeepAlive_cn
        dataSource.setKeepAlive(true);

        //连接泄露检查，打开removeAbandoned功能 , 连接从连接池借出后，长时间不归还，将触发强制回连接。回收周期随timeBetweenEvictionRunsMillis进行，如果连接为从连接池借出状态，并且未执行任何sql，并且从借出时间起已超过removeAbandonedTimeout时间，则强制归还连接到连接池中。
        dataSource.setRemoveAbandoned(true);
        //超时时间，秒
        dataSource.setRemoveAbandonedTimeout(80);
        //关闭abanded连接时输出错误日志，这样出现连接泄露时可以通过错误日志定位忘记关闭连接的位置
        dataSource.setLogAbandoned(true);
        return dataSource;
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
//        RegistryCenterConfiguration regConfig = new RegistryCenterConfiguration();
//        regConfig.setServerLists("zk1.host.dxy:2181,zk2.host.dxy:2181,zk3.host.dxy:2181");
//        regConfig.setNamespace("sharding-sphere-orchestration");
        // 配置数据治理
//        OrchestrationConfiguration orchConfig = new OrchestrationConfiguration("orchestration-sharding-data-source", regConfig, false);

        Properties properties = new Properties();
        properties.setProperty("sql.show","true");

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);


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
        Properties properties = new Properties();
        properties.setProperty("sql.show","true");
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,masterSlaveRuleConfiguration,
                new HashMap<>(),properties);
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

    @Bean("shardingJdbcTemplate")
    public JdbcTemplate shardingJdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

}
