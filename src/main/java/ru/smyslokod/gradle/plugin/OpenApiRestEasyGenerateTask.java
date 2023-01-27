package ru.smyslokod.gradle.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ru.smyslokod.gradle.plugin.model.EnumValuator;
import ru.smyslokod.gradle.plugin.model.ServiceSpecification;
import ru.smyslokod.gradle.plugin.model.ServiceSpecificationYamlReader;
import ru.smyslokod.gradle.plugin.yaml.YamlParser;
import ru.smyslokod.gradle.plugin.yaml.YamlRoot;

public class OpenApiRestEasyGenerateTask  extends DefaultTask {

    public final Path openApiSchemaPath = Paths.get("src", "main", "resources", "openapi-schema.yml");

    @TaskAction
    public void generate() throws IOException {
        System.out.println("Generating Rest Easy interface and transfer objects from OpenApi...");
        if (Files.isReadable(openApiSchemaPath)) {
            List<String> schemaYamlContent = Files.readAllLines(openApiSchemaPath);
            System.out.println("Schema length: "
                    + schemaYamlContent.stream()
                    .reduce(
                            0,
                            (size, line) -> size + line.length(),
                            (sizeLeft, sizeRight) -> sizeLeft + sizeRight)
            );
            YamlRoot schemaYaml = new YamlParser().parse(schemaYamlContent);
//            System.out.println("Found " + schemaYaml.resources.size() + " resources");
//            System.out.println("Found " + schemaYaml.components.schemas.types.size() + " types");
            ServiceSpecification specification = new ServiceSpecificationYamlReader(new EnumValuator()).readYaml(schemaYaml);
            System.out.println("Found " + specification.resources.size() + " resources");
            System.out.println("Found " + specification.components.schemas.types.size() + " types");
        } else {
            System.out.println("src/main/resources/openapi-schema.yml is not found");
        }
        System.out.println("...Done!");
    }
}
