package com.zzy.webcut.common;

public class ContentTooLongException extends RuntimeException{
    public ContentTooLongException(String message) {
        super(message);
    }
}
