package ru.smyslokod.gradle.plugin.generator;

import java.util.Collections;

import ru.smyslokod.gradle.plugin.model.ServiceSpecification;
import ru.smyslokod.gradle.plugin.model.SourceCodeFile;

public class RestEasyMutinyInterfaceGenerator {

    public SourceCodeFile generate(ServiceSpecification specification) {
        return new SourceCodeFile("Delete.java", Collections.emptyList());
    }
}
