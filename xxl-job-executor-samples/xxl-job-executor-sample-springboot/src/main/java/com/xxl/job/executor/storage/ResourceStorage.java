package com.xxl.job.executor.storage;

import com.xxl.job.executor.model.XxlJobResource;
import com.xxl.job.executor.system.monitor.ResourceBeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangsk01
 */
@Repository
public class ResourceStorage {

    @Autowired
    private NamedParameterJdbcTemplate db;

    public void updateResource(ResourceBeat resourceBeat) {
        String sql = "INSERT INTO xxl_job_resource(threadCount, cpuCount, runningCpu, remainsCpu, totalMemory, remainsMemory, usedMemory, address)" +
                " VALUES(:threadCount, :cpuCount, :runningCpu, :remainsCpu, :totalMemory, :remainsMemory, :usedMemory, :address)" +
                "ON DUPLICATE KEY UPDATE threadCount = :threadCount, cpuCount = :cpuCount, runningCpu = :runningCpu, " +
                "remainsCpu = :remainsCpu, totalMemory = :totalMemory, remainsMemory = :remainsMemory, usedMemory = :usedMemory";
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(resourceBeat);
        db.update(sql, source);
    }

    public List<XxlJobResource> getResourceList() {
        // 最近一秒有心跳更新的resource
        String sql = "SELECT * FROM xxl_job_resource WHERE dbutime > date_sub(current_timestamp(3),interval 1 SECOND)";
        return db.query(sql, BeanPropertyRowMapper.newInstance(XxlJobResource.class));
    }
}
