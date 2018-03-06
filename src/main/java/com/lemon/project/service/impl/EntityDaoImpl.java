package com.lemon.project.service.impl;

import com.lemon.project.service.EntityDao;
import com.lemon.project.service.dto.Validate;
import com.lemon.project.utils.exception.PersistException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Table;
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
        if(where==null) return jdbcTemplate.queryForList("SELECT " + columns + " FROM " + table );
        return jdbcTemplate.queryForList("SELECT " + columns + " FROM " + table + " WHERE " + where);
    }

    @Override
    public List<Map<String, Object>> getColumn(String table, String where) {
        return getColumn(table, where, "*");
    }

    @Override
    public <T> List<Map<String, Object>> getColumn(Class<T> table, String where, String columns) {
        return getColumn(getTableNameFromClass(table), where, columns);
    }

    @Override
    public <T> List<Map<String, Object>> getColumn(Class<T> table, String where, String... fieldNames) throws PersistException {
        return getColumn(getTableNameFromClass(table), where, getColumnFromFields(table, fieldNames));
    }

    @Override
    public <T> List<Map<String, Object>> getColumn(Class<T> table, String where) {
        return getColumn(getTableNameFromClass(table), where, "*");
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
            for (int i = 0; i < len; i++) {
                colName = cols[i];
                listMap.put(colName, new LinkedList<>());
            }
            buffer.append(columns).append(" FROM ").append(table);
            if(where!=null) buffer.append(" WHERE ").append(where);
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

    @Override
    public <T> Map<String, List<String>> getColumnEfficiently(Class<T> table, String where, String columns) throws PersistException {
        return getColumnEfficiently(getTableNameFromClass(table), where, columns);
    }

    @Override
    public <T> Map<String, List<String>> getColumnEfficiently(Class<T> table, String where, String... fieldNames) throws PersistException {
        return getColumnEfficiently(getTableNameFromClass(table), where, getColumnFromFields(table, fieldNames));
    }

    @Override
    public boolean checkValidate(Validate validate) {
        try{
           String sql="SELECT ID FROM "+validate.getTable()+" WHERE "+validate.getColumn()+" = "+validate.getPreparedValue();
           return jdbcTemplate.queryForList(sql).size()==0;
        } catch (Exception e) {
            return false;
        }
    }

    private String getTableNameFromClass(Class table) {
        return ((Table)table.getAnnotation(Table.class)).name();
    }

    private String getColumnFromFields(Class table, String[] fieldNames) throws PersistException {
        StringBuffer buffer = new StringBuffer();
        try {
            int len = fieldNames.length - 1;
            for (int i = 0; i < len; i++) {
                buffer.append(table.getDeclaredField(fieldNames[i]).getAnnotation(Column.class).name()).append(",");
            }
            buffer.append(table.getDeclaredField(fieldNames[len]).getAnnotation(Column.class).name());
        }catch (NullPointerException e) {
            throw new PersistException("Not an Entity");
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return buffer.toString();
    }
}
