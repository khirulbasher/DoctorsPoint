package com.lemon.project.service.dto;

import javax.persistence.Transient;

/**
 * Created by lemon on 3/4/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
public class Validate {
    private String table;
    private String column;
    private String value;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Transient
    public String processForLike() {
        return "%"+value+"%";
    }

    public String getPreparedValue() {
        return "'"+value+"'";
    }
}
