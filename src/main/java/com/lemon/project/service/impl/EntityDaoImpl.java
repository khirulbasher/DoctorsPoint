package com.lemon.project.service.impl;

import com.lemon.project.service.EntityDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EntityDaoImpl implements EntityDao {

    private final Logger log = LoggerFactory.getLogger(EntityDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public EntityDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String,Object>> getColumn(String table, String where, String columnCommaSeparate) {
        return jdbcTemplate.queryForList("SELECT "+columnCommaSeparate+" FROM "+table+" WHERE "+where);
    }
}
