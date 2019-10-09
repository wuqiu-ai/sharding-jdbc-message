package com.fly.rsa;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.Properties;

/**
 * @author: peijiepang
 * @date 2019-08-15
 * @Description:
 */
public class DruidTest {

    public static void main(String[] args) throws Exception {
        //公钥加密、私钥解密
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.202.149:3306");
        dataSource.setUsername("root");
        dataSource.setFilters("config");
        //使用RSA解密(使用默认密钥）
        Properties properties = new Properties();
        properties.setProperty("config.decrypt","true");
        //公钥
        String publickey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIiwHpFrDijV+GzwRTzWJk8D3j3jFfhsMFJ/7k1NTvBuLgL+TdIHgaMNOIEjHpXzuvX38J3FtOK8hLrySncVGOMCAwEAAQ==";
        properties.setProperty("config.decrypt.key", publickey);
        dataSource.setConnectProperties(properties);
        dataSource.setPassword("WVMjPhfXQrIsWRo0/RCqAVvYtTU9WNVToKJohb8AlUmHwnV6vwFL+FM2CNFDMJwGHW1iCmyaUlF+sgvFdogqEA==");
        dataSource.setFilters("config");
        dataSource.getConnection();
    }
}
