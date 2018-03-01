package com.lemon.project.service;

import com.lemon.project.utils.exception.PersistException;

import java.util.List;
import java.util.Map;

public interface EntityDao {
    List<Map<String, Object>> getColumn(String table, String where, String columns); /*List<Map<String,Object>> maps = entityDao.getColumn("Country","active = true","name,id,latitude,longitude");*/

    <T> List<Map<String, Object>> getColumn(Class<T> table, String where, String columns); /*List<Map<String,Object>> maps = entityDao.getColumn("Country","active = true","name,id,latitude,longitude");*/

    <T> List<Map<String, Object>> getColumn(Class<T> table, String where, String... fieldNames) throws PersistException; /*List<Map<String,Object>> maps = entityDao.getColumn("Country","active = true","name,id,latitude,longitude");*/

    Map<String, List<String>> getColumnEfficiently(String table, String where, String columns) throws PersistException;

    <T> Map<String, List<String>> getColumnEfficiently(Class<T> table, String where, String columns) throws PersistException;

    <T> Map<String, List<String>> getColumnEfficiently(Class<T> table, String where, String... fieldNames) throws PersistException;
}
