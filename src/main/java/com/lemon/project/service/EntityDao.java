package com.lemon.project.service;

import java.util.List;
import java.util.Map;

public interface EntityDao {
    List<Map<String,Object>> getColumn(String table, String where, String columnCommaSeparate); /*List<Map<String,Object>> maps = entityDao.getColumn("Country","active = true","name,id,latitude,longitude");*/
}
