package com.lemon.project.service.impl;

import com.lemon.project.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final Logger log = LoggerFactory.getLogger(ManagementService.class);

    @Override
    public String convertToGetSet(String fieldName, boolean isGet) {
        return isGet?"get":"set"+((char)(fieldName.charAt(0)-'a'+'A'))+fieldName.substring(1);
    }
}
