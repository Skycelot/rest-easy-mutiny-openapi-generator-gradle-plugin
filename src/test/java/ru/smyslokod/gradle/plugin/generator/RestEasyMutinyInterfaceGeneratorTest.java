package ru.smyslokod.gradle.plugin.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ru.smyslokod.gradle.plugin.model.EnumValuator;
import ru.smyslokod.gradle.plugin.model.ServiceSpecification;
import ru.smyslokod.gradle.plugin.model.ServiceSpecificationYamlReader;
import ru.smyslokod.gradle.plugin.model.SourceCodeFile;
import ru.smyslokod.gradle.plugin.yaml.YamlParser;
import ru.smyslokod.gradle.plugin.yaml.YamlRoot;

public class RestEasyMutinyInterfaceGeneratorTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src", "test", "resources", "openapi-schema.yml");
        List<String> lines = Files.readAllLines(path);
        YamlParser parser = new YamlParser();
        YamlRoot root = parser.parse(lines);
        ServiceSpecificationYamlReader reader = new ServiceSpecificationYamlReader(new EnumValuator());
        ServiceSpecification specification = reader.readYaml(root);

        SourceCodeFile file = new RestEasyMutinyInterfaceGenerator().generate(specification);
        int i = 0;
    }
}
