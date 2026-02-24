# hackathon-storage-lib

Biblioteca Java para compartilhamento de logica de storage local entre microsservicos.

## Requisitos

- Java 17+
- Maven 3.9+

## Build

```bash
mvn clean test
```

## Publicacao Maven

A lib esta configurada para publicar no GitHub Packages Maven Registry via `mvn deploy`.

### Configuracoes do repositorio

1. Em `Settings > Actions > General`, habilite permissao de escrita para `GITHUB_TOKEN`.
2. Em `Settings > Packages`, permita publicacao para o repositorio.

### Pipeline de publicacao

- Arquivo: `.github/workflows/publish.yml`
- Trigger: push de tag no formato `v*` (exemplo: `v1.0.0`)
- Publicacao: executa `mvn clean deploy`

Exemplo de release:

```bash
git tag v1.0.0
git push origin v1.0.0
```

### Dependencia nos microsservicos

```xml
<dependency>
    <groupId>br.com.fiap</groupId>
    <artifactId>hackathon-storage-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

Se quiser usar snapshots:

```xml
<dependency>
    <groupId>br.com.fiap</groupId>
    <artifactId>hackathon-storage-lib</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

## Como usar

### 1. Configure como bean no Spring

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

### 2. Interface disponivel

```java
public interface VideoStorageService {
    String store(InputStream inputStream, String fileName);
    InputStream retrieve(String storagePath);
}
```

## Excecoes

- `FileStorageException`
- `FileRetrievalException`
- `StoredFileNotFoundException`
