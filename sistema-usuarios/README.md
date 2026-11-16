# Sistema de usuários

## Java runtime

This application targets Java 21. Its Maven compiler release is set to `21`, so
the compiled classes require Java 21 or a newer runtime. Use a JDK 21 or newer
to build the project.

From this directory, run the test suite with:

```shell
mvn clean test
```

The test suite checks that the generated test class files use Java 21 bytecode
(class-file version 65), helping prevent accidental changes to the runtime
baseline.
