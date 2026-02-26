package br.com.fiap.storage.local;

import br.com.fiap.storage.exception.FileDeletionException;
import br.com.fiap.storage.exception.StoredFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalVideoStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldStoreAndRetrieveFile() throws IOException {
        LocalVideoStorageService service = new LocalVideoStorageService(tempDir);
        byte[] content = "video-content".getBytes();

        String storedPath = service.store(new ByteArrayInputStream(content), "video.mp4");

        assertTrue(storedPath.contains("video.mp4"));

        try (InputStream inputStream = service.retrieve(storedPath)) {
            byte[] loaded = inputStream.readAllBytes();
            assertArrayEquals(content, loaded);
        }
    }

    @Test
    void shouldThrowWhenFileDoesNotExist() {
        LocalVideoStorageService service = new LocalVideoStorageService(tempDir);

        assertThrows(StoredFileNotFoundException.class, () ->
            service.retrieve(tempDir.resolve("missing.mp4").toString())
        );
    }

    @Test
    void shouldDeleteStoredFile() throws IOException {
        LocalVideoStorageService service = new LocalVideoStorageService(tempDir);
        byte[] content = "video-content".getBytes();
        String storedPath = service.store(new ByteArrayInputStream(content), "video.mp4");

        service.delete(storedPath);

        assertThrows(StoredFileNotFoundException.class, () -> service.retrieve(storedPath));
    }

    @Test
    void shouldDeleteBeIdempotentWhenFileDoesNotExist() throws IOException {
        LocalVideoStorageService service = new LocalVideoStorageService(tempDir);
        Path nonExistentPath = tempDir.resolve("non-existent.mp4");

        service.delete(nonExistentPath.toString());

        assertTrue(Files.notExists(nonExistentPath));
    }

    @Test
    void shouldThrowWhenDeletePathIsOutsideStorage() throws IOException {
        LocalVideoStorageService service = new LocalVideoStorageService(tempDir);
        Path outsidePath = tempDir.getParent().resolve("outside.txt");
        Files.createFile(outsidePath);

        assertThrows(FileDeletionException.class, () ->
            service.delete(outsidePath.toString())
        );

        Files.deleteIfExists(outsidePath);
    }
}
