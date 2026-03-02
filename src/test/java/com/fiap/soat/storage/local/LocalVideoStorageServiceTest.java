package com.fiap.soat.storage.local;

import com.fiap.soat.storage.exception.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class LocalVideoStorageServiceTest {

    @TempDir
    Path tempDir;

    private LocalVideoStorageService storageService;

    @BeforeEach
    void setup() {
        storageService = new LocalVideoStorageService(tempDir);
    }

    @Test
    void shouldThrowWhenBaseDirIsNull_StringConstructor() {
        assertThrows(NullPointerException.class,
                () -> new LocalVideoStorageService((String) null));
    }

    @Test
    void shouldThrowWhenBaseDirIsNull_PathConstructor() {
        assertThrows(NullPointerException.class,
                () -> new LocalVideoStorageService((Path) null));
    }

    @Test
    void shouldStoreFileSuccessfully() {
        String content = "test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        String storedPath = storageService.store(inputStream, "video.mp4");

        Path filePath = Paths.get(storedPath);

        assertTrue(Files.exists(filePath));
        assertTrue(storedPath.contains("video.mp4"));
    }

    @Test
    void shouldCreateDirectoryIfNotExists() {
        Path newDir = tempDir.resolve("subdir");
        LocalVideoStorageService service = new LocalVideoStorageService(newDir);

        InputStream inputStream = new ByteArrayInputStream("abc".getBytes());

        String storedPath = service.store(inputStream, "file.txt");

        assertTrue(Files.exists(Paths.get(storedPath)));
    }

    @Test
    void shouldThrowWhenInputStreamIsNull() {
        assertThrows(NullPointerException.class,
                () -> storageService.store(null, "file.txt"));
    }

    @Test
    void shouldThrowWhenFileNameIsNull() {
        InputStream inputStream = new ByteArrayInputStream("abc".getBytes());

        assertThrows(NullPointerException.class,
                () -> storageService.store(inputStream, null));
    }

    @Test
    void shouldThrowFileStorageExceptionWhenCopyFails() throws Exception {
        try (InputStream brokenStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("fail");
            }
        }) {
            assertThrows(FileStorageException.class,
                    () -> storageService.store(brokenStream, "file.txt"));
        }
    }

    @Test
    void shouldRetrieveFileSuccessfully() throws Exception {
        Path file = tempDir.resolve("file.txt");
        Files.write(file, "content".getBytes());

        InputStream result = storageService.retrieve(file.toString());

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenStoragePathIsNull_Retrieve() {
        assertThrows(NullPointerException.class,
                () -> storageService.retrieve(null));
    }

    @Test
    void shouldThrowStoredFileNotFoundExceptionWhenFileDoesNotExist() {
        String fakePath = tempDir.resolve("notfound.txt").toString();

        assertThrows(StoredFileNotFoundException.class,
                () -> storageService.retrieve(fakePath));
    }

    @Test
    void shouldThrowFileRetrievalExceptionWhenIOExceptionOccurs() throws Exception {

        Path file = tempDir.resolve("file.txt");
        Files.createFile(file);

        String filePath = file.toString();

        LocalVideoStorageService service =
                new LocalVideoStorageService(tempDir) {
                    @Override
                    protected InputStream openFile(Path path) throws IOException {
                        throw new IOException("forced error");
                    }
                };

        assertThrows(FileRetrievalException.class,
                () -> service.retrieve(filePath));
    }

    @Test
    void shouldDeleteFileSuccessfully() throws Exception {
        Path file = tempDir.resolve("file.txt");
        Files.write(file, "content".getBytes());

        storageService.delete(file.toString());

        assertFalse(Files.exists(file));
    }

    @Test
    void shouldThrowWhenStoragePathIsNull_Delete() {
        assertThrows(NullPointerException.class,
                () -> storageService.delete(null));
    }

    @Test
    void shouldThrowWhenPathIsOutsideBaseDirectory() {
        Path outsidePath = Paths.get(System.getProperty("java.io.tmpdir"))
                .resolve("outside.txt");

        String outsidePathStr = outsidePath.toString();

        assertThrows(FileDeletionException.class,
                () -> storageService.delete(outsidePathStr));
    }

    @Test
    void shouldThrowFileDeletionExceptionWhenDeleteFails() throws Exception {
        Path dir = tempDir.resolve("nonEmptyDir");
        Files.createDirectory(dir);
        Files.write(dir.resolve("file.txt"), "content".getBytes());

        String dirPath = dir.toString();

        assertThrows(FileDeletionException.class,
                () -> storageService.delete(dirPath));
    }
}