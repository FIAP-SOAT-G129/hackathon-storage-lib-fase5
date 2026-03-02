package com.fiap.soat.storage;

import com.fiap.soat.storage.exception.FileRetrievalException;
import com.fiap.soat.storage.exception.FileStorageException;
import com.fiap.soat.storage.exception.StoredFileNotFoundException;

import java.io.InputStream;

public interface VideoStorageService {

    String store(InputStream inputStream, String fileName) throws FileStorageException;

    InputStream retrieve(String storagePath) throws StoredFileNotFoundException, FileRetrievalException;

    void delete(String storagePath);
}
