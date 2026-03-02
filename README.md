# 📦 Storage Lib — Abstração e Segurança de Arquivos

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

![Coverage](../hackathon-storage-lib-fase5/.github/badges/jacoco.svg)
![Branches](../hackathon-storage-lib-fase5/.github/badges/branches.svg)

Esta é a **Storage Lib**, uma biblioteca Java pura desenvolvida para o
**Hackathon SOAT (Fase 5)**. Ela fornece uma camada de abstração robusta
para operações de I/O, garantindo que o armazenamento de vídeos e
arquivos ZIP em microserviços seja padronizado, seguro e independente de
infraestrutura.

---

## 🧾 Objetivo do Projeto

Centralizar a lógica de persistência e recuperação de arquivos,
fornecendo validações de domínio rigorosas e proteção nativa contra
vulnerabilidades de sistema de arquivos (como Path Traversal).\
Projetada para ser acoplada em microserviços Spring Boot ou aplicações
Java puras que necessitam de uma gestão de arquivos organizada e segura.

---

## 🚀 Tecnologias Utilizadas

-   **Java 17**
-   **Maven**
-   **JUnit 5**
-   **Mockito**

---

## ⚙️ Como Utilizar

### ✅ Instalação Maven

``` xml
<dependency>
    <groupId>com.fiap.soat</groupId>
    <artifactId>storage-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

------------------------------------------------------------------------

## 🧪 Executando os testes

``` bash
mvn test
mvn clean verify
```

------------------------------------------------------------------------

## 👥 Equipe

Desenvolvido pela equipe FIAP SOAT - G129 para o Hackathon de
Arquitetura de Software.

------------------------------------------------------------------------

## 📄 Licença

Este projeto é parte integrante do material acadêmico da FIAP.
