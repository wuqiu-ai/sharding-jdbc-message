package com.fly.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 * @author: peijiepang
 * @date 2018/11/15
 * @Description:
 */
@RestController
public class ShardingTestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShardingTestController.class);

    @Qualifier("shardingJdbcTemplate")
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String test() throws InterruptedException {
        return "ok";
    }

    @PostMapping("/sharding/sqltest")
    public String sqltest(@RequestParam("sql")String sql){
        Optional.ofNullable(sql).map(t->t.trim())
                .orElseThrow( () -> new IllegalArgumentException("sql不能为空"));
        StringTokenizer st = new StringTokenizer(sql," ,?.!:\"\"''\n#");
        String firstToken = st.nextToken();
        LOGGER.debug("firstToken:{} sql:{}",firstToken,sql);
        if("insert".equalsIgnoreCase(firstToken)){
            int count = jdbcTemplate.update(sql);
            if(count == 0){
                throw new IllegalArgumentException("insert fail");
            }
        }else if("update".equalsIgnoreCase(firstToken)){
            int count = jdbcTemplate.update(sql);
            if(count == 0){
                throw new IllegalArgumentException("insert fail");
            }
        }else if("delete".equalsIgnoreCase(firstToken)){
            int count = jdbcTemplate.update(sql);
            if(count == 0){
                throw new IllegalArgumentException("insert fail");
            }
        }else if("select".equalsIgnoreCase(firstToken)){
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            LOGGER.info("select list:{}",JSON.toJSONString(list));
        }else
            throw new IllegalArgumentException("不支持sql");
        return "ok";
    }
}
