package ru.smyslokod.gradle.plugin.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class YamlArray extends YamlRoot {

    private final List<YamlRoot> items = new ArrayList<>();

    public void add(YamlRoot item) {
        items.add(item);
    }

    public Spliterator<YamlRoot> spliterator() {
        return items.spliterator();
    }
}
