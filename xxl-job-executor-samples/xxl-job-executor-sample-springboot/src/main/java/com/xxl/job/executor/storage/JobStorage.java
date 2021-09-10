package com.xxl.job.executor.storage;

import com.xxl.job.executor.model.XxlJobGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


/**
 * @author zhangsk01
 */
@Repository
public class JobStorage {

    @Autowired
    private NamedParameterJdbcTemplate db;

    public XxlJobGroup getJobGroup(int id) {
        String sql = "SELECT * FROM xxl_job_group where id = :id";
        HashMap<String, Integer> map = new HashMap<>();
        map.put("id", id);
        return db.queryForObject(sql, map, BeanPropertyRowMapper.newInstance(XxlJobGroup.class));
    }
}
