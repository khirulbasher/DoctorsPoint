package com.lemon.project.service.impl;

import com.lemon.project.service.EntityDao;
import com.lemon.project.utils.exception.PersistException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection", "StringBufferMayBeStringBuilder"})
@Service
@Transactional
public class EntityDaoImpl implements EntityDao {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public EntityDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getColumn(String table, String where, String columns) {
        return jdbcTemplate.queryForList("SELECT " + columns + " FROM " + table + " WHERE " + where);
    }

    @Override
    public Map<String, List<String>> getColumnEfficiently(String table, String where, String columns) throws PersistException {
        String[] cols = columns.split(",");
        int len = cols.length;
        if (len <= 0) throw new PersistException("No Column Selected");
        Map<String, List<String>> listMap;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); Statement statement = connection.createStatement()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("SELECT ");
            String colName;
            listMap = new HashMap<>();
            for (int i = 0; i < len - 1; i++) {
                colName = cols[i];
                buffer.append(colName).append(" , ");
                listMap.put(colName, new LinkedList<>());
            }
            colName = cols[len - 1];
            buffer.append(colName).append(" FROM ").append(table).append(" WHERE ").append(where);
            listMap.put(colName, new LinkedList<>());
            ResultSet resultSet = statement.executeQuery(buffer.toString());
            Set<String> columnSet = listMap.keySet();
            while (resultSet.next()) {
                for (String columnName : columnSet) {
                    listMap.get(columnName).add(resultSet.getObject(columnName).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return listMap;
    }
}
