package br.com.fiap.storage.local;

import br.com.fiap.storage.exception.StoredFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
}
