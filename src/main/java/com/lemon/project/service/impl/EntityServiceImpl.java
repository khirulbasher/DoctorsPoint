package com.lemon.project.service.impl;

import com.lemon.project.domain.Transaction;
import com.lemon.project.domain.User;
import com.lemon.project.domain.enumeration.TransactionType;
import com.lemon.project.repository.UserRepository;
import com.lemon.project.security.SecurityUtils;
import com.lemon.project.service.EntityService;
import com.lemon.project.service.ManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by lemon on 3/3/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows", "unchecked"})
@Service
@Transactional
public class EntityServiceImpl implements EntityService {
    private final ManagementService managementService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Inject
    public EntityServiceImpl(ManagementService managementService, SecurityUtils securityUtils, UserRepository userRepository) {
        this.managementService = managementService;
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    @Override
    public <K> void modify(K entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        modifyFinally(entity, "setLastModifyDate", "setLastModifiedBy");
    }

    @Override
    public <K> void modify(K entity, String date, String person) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        modifyFinally(entity, managementService.convertToGetSet(date, false), managementService.convertToGetSet(person, false));
    }

    @Override
    public <K> void modify(K entity, String[] fields, Class[] types, Object[] values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        int len = fields.length;
        Class aClass = entity.getClass();
        for (int i = 0; i < len; i++)
            aClass.getMethod(fields[i], types[i]).invoke(entity, values[i]);
    }

    @Override
    public Transaction transaction(BigDecimal amount, TransactionType transactionType, User toUser) {
        return transaction("NO Reason",amount,transactionType,toUser);
    }

    @Override
    public Transaction transaction(String reason, BigDecimal amount, TransactionType transactionType, User toUser) {
        return new Transaction(reason,amount,transactionType,userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get(),toUser);
    }

    private <K> void modifyFinally(K entity, String date, String person) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        modify(entity, new String[]{date, person}, new Class[]{LocalDate.class, Long.class}, new Object[]{LocalDate.now(), securityUtils.getCurrentUserId()});
    }


}
