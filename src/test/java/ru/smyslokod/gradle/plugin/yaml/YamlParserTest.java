package ru.smyslokod.gradle.plugin.yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class YamlParserTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src", "test", "resources", "openapi-schema.yml");
        List<String> lines = Files.readAllLines(path);
        YamlParser parser = new YamlParser();
        YamlRoot root = parser.parse(lines);
        int i = 0;
    }
}
