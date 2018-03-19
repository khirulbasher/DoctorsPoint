package com.lemon.project.service;

import com.lemon.project.domain.Cash;
import com.lemon.project.domain.Transaction;
import com.lemon.project.domain.User;
import com.lemon.project.domain.enumeration.TransactionType;
import com.lemon.project.security.SecurityUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lemon on 3/3/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows", "unchecked"})
public interface EntityService {
    <K> void modify(K entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    <K> void modify(K entity, String date, String person) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    <K> void modify(K entity, String[] fields, Class[] types, Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    static String getSequence(String prefix) {
        return getSequence(prefix,"");
    }

    static String getSequence(String prefix, String postfix) {
        return prefix+System.currentTimeMillis()+postfix;
    }

    Transaction transaction(BigDecimal amount, TransactionType transactionType, User toUser);

    Transaction transaction(String reason, BigDecimal amount, TransactionType transactionType, User toUser);
}
