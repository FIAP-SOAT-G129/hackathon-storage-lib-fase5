# hackathon-storage-lib

Biblioteca Java para compartilhamento de lógica de storage local entre microsserviços.

## Requisitos

- Java 17+
- Maven 3.9+

## Build

```bash
mvn clean test
```

## Como usar

### 1. Adicione a dependência no seu microsserviço

Publique a lib no seu repositório Maven interno e adicione:

```xml
<dependency>
    <groupId>br.com.fiap</groupId>
    <artifactId>hackathon-storage-lib</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Configure como bean no Spring

```java
import br.com.fiap.storage.VideoStorageService;
import br.com.fiap.storage.local.LocalVideoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public VideoStorageService videoStorageService(
            @Value("${app.storage.local-path:/tmp/videos}") String storageBaseDir) {
        return new LocalVideoStorageService(storageBaseDir);
    }
}
```

### 3. Interface disponível

```java
public interface VideoStorageService {
    String store(InputStream inputStream, String fileName);
    InputStream retrieve(String storagePath);
}
```

## Exceções

- `FileStorageException`
- `FileRetrievalException`
- `StoredFileNotFoundException`
