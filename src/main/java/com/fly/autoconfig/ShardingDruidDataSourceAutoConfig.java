package com.fly.autoconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.dxy.keygen.shardingjdbc.ShardingDefaultKeyGenerator;
import com.fly.algorithm.MyPreciseShardingAlgorithm;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.NoneShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
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


//    @Bean
//    public SnowFlakeKeyGenerator snowFlakeKeyGenerator(){
//        MachineConfig machineConfig = new ZookeeperMachineConfig();
//        ((ZookeeperMachineConfig) machineConfig).setConnectionInfo("zk1.host.dxy:2181,zk2.host.dxy:2181,zk3.host.dxy:2181");
//        ((ZookeeperMachineConfig) machineConfig).setEnv("dev");
//        ((ZookeeperMachineConfig) machineConfig).setBusinessCode("test");
////        MachineConfig machineConfig = new SystemPropertyMachineConfig();
//        SnowFlakeKeyGenerator keyGenerator = new DefaultKeyGenerator(machineConfig);
//        return keyGenerator;
//    }

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

        //是否缓存preparedStatement，也就是PSCachef。使用sharding-jdbc的时候，一定要打开该操作，用于缓存sql解析器的缓存
        dataSource.setPoolPreparedStatements(true);

        //连接泄露检查，打开removeAbandoned功能 , 连接从连接池借出后，长时间不归还，将触发强制回连接。回收周期随timeBetweenEvictionRunsMillis进行，如果连接为从连接池借出状态，并且未执行任何sql，并且从借出时间起已超过removeAbandonedTimeout时间，则强制归还连接到连接池中。
        dataSource.setRemoveAbandoned(true);
        //超时时间，秒
        dataSource.setRemoveAbandonedTimeout(80);
        //关闭abanded连接时输出错误日志，这样出现连接泄露时可以通过错误日志定位忘记关闭连接的位置
        dataSource.setLogAbandoned(true);

        //通过connectProperties属性来打开mergeSql功能；慢SQL记录
        Properties properties = new Properties();
        properties.setProperty("druid.stat.mergeSql","true");
        properties.setProperty("druid.stat.slowSqlMillis","5000");
        dataSource.setConnectProperties(properties);
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
        dataSourceMap.put("ds0",ds0DataSource());
        dataSourceMap.put("ds1",ds1DataSource());
        dataSourceMap.put("ds2",ds2DataSource());
        dataSourceMap.put("ds3",ds3DataSource());
        dataSourceMap.put("mainDataSource",mainDataSource());

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("push_message");
        orderTableRuleConfig.setActualDataNodes("ds${0..3}.push_message");

        //分布式主键
        orderTableRuleConfig.setKeyGeneratorColumnName("id");
        ShardingDefaultKeyGenerator shardingDefaultKeyGenerator = new ShardingDefaultKeyGenerator();
        orderTableRuleConfig.setKeyGenerator(shardingDefaultKeyGenerator);

        // 配置分库策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("traceId", "ds${traceId%4}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.setDefaultDataSourceName("mainDataSource");

        Properties properties = new Properties();
        properties.setProperty("sql.show","true");
        properties.setProperty("max.connections.size.per.query","200");
        properties.setProperty("executor.size",String.valueOf(Runtime.getRuntime().availableProcessors() * 2));

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);
        return dataSource;
    }

    /**
     * 1个分片数据源
     * @return
     * @throws SQLException
     */
    @Bean(name = "dataSource1",autowire = Autowire.BY_NAME)
    public DataSource dataSource1() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("main",mainDataSource());

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("push_message");
        orderTableRuleConfig.setActualDataNodes("main.push_message");

        ShardingDefaultKeyGenerator shardingDefaultKeyGenerator = new ShardingDefaultKeyGenerator();
        orderTableRuleConfig.setKeyGenerator(shardingDefaultKeyGenerator);
        orderTableRuleConfig.setKeyGeneratorColumnName("id");
        // 配置分片规则
        NoneShardingStrategyConfiguration noneShardingStrategyConfiguration = new NoneShardingStrategyConfiguration();
        orderTableRuleConfig.setTableShardingStrategyConfig(noneShardingStrategyConfiguration);

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        Properties properties = new Properties();
        properties.setProperty("sql.show","true");
        properties.setProperty("max.connections.size.per.query","200");
        properties.setProperty("executor.size",String.valueOf(Runtime.getRuntime().availableProcessors() * 2));
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);
        return dataSource;
    }

    /**
     * 2个分片的分库分表
     * @return
     * @throws SQLException
     */
    @Bean(name = "dataSource3",autowire = Autowire.BY_NAME)
    public DataSource dataSource3() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0",ds0DataSource());
        dataSourceMap.put("ds1",ds1DataSource());

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("push_message");
        orderTableRuleConfig.setActualDataNodes("ds${0..1}.push_message2");

        ShardingDefaultKeyGenerator shardingDefaultKeyGenerator = new ShardingDefaultKeyGenerator();
        orderTableRuleConfig.setKeyGenerator(shardingDefaultKeyGenerator);
        orderTableRuleConfig.setKeyGeneratorColumnName("id");

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("traceid", "ds${traceid % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        Properties properties = new Properties();
        properties.setProperty("sql.show","true");
        properties.setProperty("max.connections.size.per.query","200");
        properties.setProperty("executor.size",String.valueOf(Runtime.getRuntime().availableProcessors() * 2));
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);
        return dataSource;
    }

    /**
     * 单库多表
     * @return
     * @throws SQLException
     */
    @Bean(name = "dataSource4",autowire = Autowire.BY_NAME)
    public DataSource dataSource4() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0",ds0DataSource());

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("push_message");
        orderTableRuleConfig.setActualDataNodes("ds0.push_message,ds0.push_message2,ds0.push_message3,ds0.push_message4");

        ShardingDefaultKeyGenerator shardingDefaultKeyGenerator = new ShardingDefaultKeyGenerator();
        orderTableRuleConfig.setKeyGenerator(shardingDefaultKeyGenerator);
        orderTableRuleConfig.setKeyGeneratorColumnName("id");

        // 配置分库 + 分表策略
        MyPreciseShardingAlgorithm myPreciseShardingAlgorithm = new MyPreciseShardingAlgorithm();
        StandardShardingStrategyConfiguration standardStrategy =
                new StandardShardingStrategyConfiguration("traceid",myPreciseShardingAlgorithm);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(standardStrategy);

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        Properties properties = new Properties();
        properties.setProperty("sql.show","true");
        properties.setProperty("max.connections.size.per.query","200");
        properties.setProperty("executor.size",String.valueOf(Runtime.getRuntime().availableProcessors() * 2));
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(
                dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);
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

    @Bean("fourJdbcTemplate")
    public JdbcTemplate shardingJdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

    @Bean("singleJdbcTemplate")
    public JdbcTemplate singleJdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource1());
    }

    @Bean("twoJdbcTemplate")
    public JdbcTemplate twoJdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource3());
    }


    @Bean("singleDatabaseMultipleTableJdbcTemplate")
    public JdbcTemplate singleDatabaseMultipleTableJdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource4());
    }

    /**
     * jdbc 测试
     * @return
     * @throws SQLException
     */
    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() throws SQLException {
        return new JdbcTemplate(mainDataSource());
    }

}
