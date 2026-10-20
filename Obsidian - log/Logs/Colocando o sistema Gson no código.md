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