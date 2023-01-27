package ru.smyslokod.gradle.plugin.yaml;

public class YamlBoolean extends YamlRoot {

    private final boolean value;

    public YamlBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
