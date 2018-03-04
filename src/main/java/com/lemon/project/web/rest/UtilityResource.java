package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lemon on 3/3/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
@RestController
@RequestMapping("/out")
public class UtilityResource {

    @PostMapping("/utility/checkOnDatabase")
    @Timed
    public ResponseEntity<Map<String,Boolean>> checkOnDatabase(@RequestBody String table, @RequestBody String column, @RequestBody String value) {
        Map<String,Boolean> map=new HashMap<>();
        boolean isOk=false;
        map.put("isOk",isOk);
        return Optional.ofNullable(map).map(val->new ResponseEntity<>(val, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
