package com.lemon.project.utility.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by lemon on 3/22/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
public class PageUtil implements Pageable {
    private int size;
    private int page;

    public PageUtil(int size, int page) {
        this.size = size;
        this.page = page;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public Pageable next() {
        return new PageUtil(size,page+1);
    }

    @Override
    public Pageable previousOrFirst() {
        return page>0?new PageUtil(size,page-1):first();
    }

    @Override
    public Pageable first() {
        return new PageUtil(size,0);
    }

    @Override
    public boolean hasPrevious() {
        return page>0;
    }
}
