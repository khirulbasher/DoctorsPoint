package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.security.AuthoritiesConstants;
import com.lemon.project.service.EntityDao;
import com.lemon.project.service.dto.Validate;
import com.lemon.project.utils.exception.PersistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lemon on 3/3/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
@RestController
@RequestMapping("/out")
public class UtilityResource {
    private final EntityDao entityDao;
    private static final String[] secureTables;
    private static final String[] secureColumns;

    static {
        secureTables=new String[]{"user"};
        secureColumns=new String[]{"passwords"};
    }

    @Inject
    public UtilityResource(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    @PostMapping("/utility/checkOnDatabase")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.ROLE_MGT})
    public ResponseEntity<Map<String,Boolean>> checkOnDatabase(@RequestBody Validate validate) throws IllegalAccessException {
        checkTable(validate.getTable(),"");
        Map<String,Boolean> map=new HashMap<>();
        map.put("isUnique",entityDao.checkValidate(validate));
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @GetMapping("/utility/custom_query/{table}/{cols}/{where}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.ROLE_MGT})
    public ResponseEntity<List<Map<String,Object>>> queryGet(@PathVariable String table,@PathVariable String cols,@PathVariable String where) throws PersistException, IllegalAccessException {
        checkTable(table,cols);
        return new ResponseEntity<>(entityDao.getColumn(table,where.equals("NULL")?null:where,cols),HttpStatus.OK);
    }

    @GetMapping("/utility/findOne/{table}/{where}")
    @Timed
    public ResponseEntity<Map<String,Object>> findOne(@PathVariable String table,@PathVariable String where) throws IllegalAccessException {
        checkTable(table,"");
        return Optional.ofNullable(entityDao.getColumn(table,where).get(0)).map(val->new ResponseEntity<>(val,HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private void checkTable(String table, String columns) throws IllegalAccessException {
        table=table.toLowerCase();
        columns=columns.toLowerCase();
        for(String tab:secureTables)
            if(table.contains(tab)) throw new IllegalAccessException("Permission Denied");
        if(columns.isEmpty()) return;
        for (String column:secureColumns)
            if(columns.contains(column)) throw new IllegalAccessException("Permission Denied");
    }
}
