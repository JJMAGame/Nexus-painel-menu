O maior desafio é incluir um sistema json dentro do código usando o sistema e depedencia gson

```java fold:JsonUtil
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final String ARQUIVO = "usuarios.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Lê a lista de usuários do arquivo JSON

    public static List<Usuario> lerUsuarios() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();



        try (Reader reader = new FileReader(file)) {
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, tipoLista);
            return usuarios != null ? usuarios : new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON", e);

        }
    }
}
```
## Problemas:

mesmo que o sistema gson.jar tenha sido instalado corretamente, começou a ter alguns problemas incomuns então estou tentando localizar o problema para ter a efetividade do programa, tentei colocar o pom.xml, mas não sei realmente se foi efetivo para resolver o problema

```xml fold:pom
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

O que foi notado que o sistema no settings.json realmente funcionou mas o sistema não esta detectando o ==🟡"com."== coisa que eu tenho que ver futuramente.

```json fold:settings
{
    "java.project.referencedLibraries": [
        "D:\\GITHUB REPOSITORY\\Nexus-painel-menu\\lib\\gson-2.10.1.jar",
        "lib/**/*.jar",
    ]
}
```

### Imagem que explicar melhor o problema

![[Pasted image 20261019171733.png]]

O sistema esta detectando bem o todo o resto do código mas não pega o com. irei ver isso futuramente e atualizarei esse documento

---
# Parte 2

No momento que eu estou colocando esse texto estou me arrumando para a faculdade, conseguir construir um sistema Maven e junto com a dependencia Gson, agora eu consigo montar um banco de dados localmente

## *Antes*

![[Pasted image 20261020151001.png]]

## *Depois*

![[Pasted image 20261020151040.png]]

```xml fold:Todo-o-sistema-pom hl:29-33

<?xml version="1.0" encoding="UTF-8"?>

  

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"

  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  
  <modelVersion>4.0.0</modelVersion>  
  <groupId>com.nexusmaven</groupId>
  <artifactId>sistema-usuarios</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>sistema-usuarios</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>  
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.10.1</version>
    </dependency>
  </dependencies>
  
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
         <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.7.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.0.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```
 
 ---
# Parte 3