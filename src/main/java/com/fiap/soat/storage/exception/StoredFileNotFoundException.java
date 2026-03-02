package com.fiap.soat.storage.exception;

public class StoredFileNotFoundException extends RuntimeException {

    public StoredFileNotFoundException(String message) {
        super(message);
    }
}
