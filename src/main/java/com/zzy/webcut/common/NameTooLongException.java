package com.zzy.webcut.common;

public class NameTooLongException extends RuntimeException{
    public NameTooLongException(String message) {
        super(message);
    }
}
