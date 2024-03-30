package com.zzy.webcut.common;

public class ClipboardNotFoundException extends RuntimeException{
    public ClipboardNotFoundException(String message) {
        super(message);
    }
}
