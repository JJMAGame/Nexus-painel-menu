# Sistema de usuários

## Java runtime

This application targets Java 25. Its Maven compiler release is set to `25`, so
the compiled classes require Java 25 or a newer runtime. Use a JDK 25 or newer
to build the project.

From this directory, run the test suite with:

```shell
mvn clean test
```

The test suite checks that the generated test class files use Java 25 bytecode
(class-file version 69), helping prevent accidental changes to the runtime
baseline.

## Executar

Os bancos de dados são os arquivos `usuarios.json` e `imoveis.json` na **raiz do
repositório**, e o programa os procura na pasta de onde ele é executado. Rode a
partir da raiz (`Nexus-painel-menu/`), não de dentro de `sistema-usuarios/`.
Os arquivos JSON sempre são lidos e gravados em UTF-8.

## Padrões do código

- Classes e arquivos em PascalCase (`Admin`, `Inquilino`, `Main`); métodos e variáveis em camelCase.
- Cada usuário no JSON tem o campo `"tipo"` (`admin` ou `inquilino`); sem ele, é lido como inquilino.
- A pasta `target/` é gerada pelo Maven e não vai para o Git (veja o `.gitignore`).

