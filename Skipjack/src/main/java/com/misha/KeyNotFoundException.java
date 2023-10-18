package com.misha;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException() {
        super("Ключ не обнаружен");
    }
}

