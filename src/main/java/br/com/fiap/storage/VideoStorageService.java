package br.com.fiap.storage;

import br.com.fiap.storage.exception.FileRetrievalException;
import br.com.fiap.storage.exception.FileStorageException;
import br.com.fiap.storage.exception.StoredFileNotFoundException;

import java.io.InputStream;

public interface VideoStorageService {

    String store(InputStream inputStream, String fileName) throws FileStorageException;

    InputStream retrieve(String storagePath) throws StoredFileNotFoundException, FileRetrievalException;
}
