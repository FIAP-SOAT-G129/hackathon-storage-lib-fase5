package com.fiap.soat.storage.local;

import com.fiap.soat.storage.VideoStorageService;
import com.fiap.soat.storage.exception.FileDeletionException;
import com.fiap.soat.storage.exception.FileRetrievalException;
import com.fiap.soat.storage.exception.FileStorageException;
import com.fiap.soat.storage.exception.StoredFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class LocalVideoStorageService implements VideoStorageService {

    private final Path storageBaseDir;

    public LocalVideoStorageService(String storageBaseDir) {
        this(Paths.get(Objects.requireNonNull(storageBaseDir, "storageBaseDir must not be null")));
    }

    public LocalVideoStorageService(Path storageBaseDir) {
        this.storageBaseDir = Objects.requireNonNull(storageBaseDir, "storageBaseDir must not be null");
    }

    @Override
    public String store(InputStream inputStream, String fileName) throws FileStorageException {
        Objects.requireNonNull(inputStream, "inputStream must not be null");
        Objects.requireNonNull(fileName, "fileName must not be null");

        try {
            if (!Files.exists(storageBaseDir)) {
                Files.createDirectories(storageBaseDir);
            }

            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            Path filePath = storageBaseDir.resolve(uniqueFileName);

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new FileStorageException("Could not store file: " + fileName, e);
        }
    }

    @Override
    public InputStream retrieve(String storagePath) throws StoredFileNotFoundException, FileRetrievalException {
        Objects.requireNonNull(storagePath, "storagePath must not be null");

        try {
            Path filePath = Paths.get(storagePath);
            if (!Files.exists(filePath)) {
                throw new StoredFileNotFoundException("File not found at path: " + storagePath);
            }
            return openFile(filePath);
        } catch (IOException e) {
            throw new FileRetrievalException("Could not retrieve file: " + storagePath, e);
        }
    }

    @Override
    public void delete(String storagePath) {
        Objects.requireNonNull(storagePath, "storagePath must not be null");

        try {
            Path filePath = Paths.get(storagePath).toAbsolutePath().normalize();
            Path normalizedBase = storageBaseDir.toAbsolutePath().normalize();

            if (!filePath.startsWith(normalizedBase)) {
                throw new FileDeletionException("Path is outside storage directory: " + storagePath);
            }

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileDeletionException("Could not delete file: " + storagePath, e);
        }
    }

    protected InputStream openFile(Path path) throws IOException {
        return Files.newInputStream(path);
    }}
