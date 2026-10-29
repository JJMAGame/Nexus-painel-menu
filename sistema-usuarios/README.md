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
