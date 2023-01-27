package ru.smyslokod.gradle.plugin.yaml;

public class YamlString extends YamlRoot {

    private final String value;

    public YamlString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
