package ru.smyslokod.gradle.plugin.yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;

public class YamlObject extends YamlRoot {

    private final Map<String, YamlRoot> fields = new HashMap<>();

    public void setField(String name, YamlRoot value) {
        fields.put(name, value);
    }

    public Optional<YamlRoot> getField(String name) {
        return Optional.ofNullable(fields.get(name));
    }

    public Spliterator<String> spliterator() {
        return fields.keySet().spliterator();
    }
}
