package com.lemon.project.utils.exception;

/**
 * Created by lemon on 2/28/2018.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "StringBufferMayBeStringBuilder", "ConstantConditions", "RedundantThrows"})
public class PersistException extends Exception {
    public PersistException(Throwable throwable) {
        super(throwable);
    }

    public PersistException(String msg) {
        super(msg);
    }
}
