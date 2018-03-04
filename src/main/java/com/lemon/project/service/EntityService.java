package com.lemon.project.service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by lemon on 3/3/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
public interface EntityService {
    <K> void modify(K entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    <K> void modify(K entity, String date, String person) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    <K> void modify(K entity, String[] fields, Class[] types, Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
