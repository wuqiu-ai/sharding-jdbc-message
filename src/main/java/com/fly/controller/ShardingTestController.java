package com.fly.controller;

import com.fly.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Types;
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

    @Qualifier("fourJdbcTemplate")
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PushService pushService;

    @PostMapping("/sharding/sqltest")
    public String sqltest(@RequestParam("sql") String sql) {
        Optional.ofNullable(sql).map(t -> t.trim())
                .orElseThrow(() -> new IllegalArgumentException("sql不能为空"));
        StringTokenizer st = new StringTokenizer(sql, " ,?.!:\"\"''\n#");
        String firstToken = st.nextToken();
        LOGGER.debug("firstToken:{} sql:{}", firstToken, sql);

        if ("insert".equalsIgnoreCase(firstToken)) {
            jdbcTemplate.update(sql);
        } else if ("update".equalsIgnoreCase(firstToken)) {
            jdbcTemplate.update(sql);
        } else if ("delete".equalsIgnoreCase(firstToken)) {
            jdbcTemplate.update(sql);
        } else if ("select".equalsIgnoreCase(firstToken)) {
            jdbcTemplate.queryForList(sql);
        } else
            throw new IllegalArgumentException("不支持sql");
        return "ok";
    }

    @PostMapping("/message/test")
    public String messageTest(@RequestParam("traceId") Long traceId, @RequestParam("userName") String userName, @RequestParam("deviceToken") String deviceToken) {
        String selectSql = "SELECT * FROM push_message WHERE traceId = ? and userId=? and deviceToken=?";
        Object[] args = {traceId, userName, deviceToken};
        int[] argTypes = {Types.BIGINT, Types.VARCHAR, Types.VARCHAR};
        Map<String, Object> result = null;
        try {
            result = jdbcTemplate.queryForMap(selectSql, args, argTypes);
        } catch (DataAccessException ex) {
        }
        if (null == result) {
            return "result is null";
        }

        Object[] update_args = {result.get("id"), traceId};
        int[] update_argTypes = {Types.BIGINT, Types.BIGINT};
        String updateSql = "UPDATE push_message SET status='click' WHERE id = ? and traceId = ?";
        jdbcTemplate.update(updateSql, update_args, update_argTypes);

//        pushService.selectByTraceId(traceId);
        return "ok";
    }


}
