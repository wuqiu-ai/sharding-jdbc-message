package com.fly.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全漏洞-sql注入
 * @author: peijiepang
 * @date 2020/6/3
 * @Description:
 */
@RestController
public class SqlInjectionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SqlInjectionController.class);

    @Qualifier("jdbcTemplate")
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试sql注入接口
     * @param id
     * @return
     */
    @GetMapping("/testsqlinject")
    public Map<String, Object> sqlInjectTest(String id){
        String selectSql = "select * from push_message2 where id = "+id;
        Map<String, Object> result = null;
        try {
            result = jdbcTemplate.queryForMap(selectSql);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
