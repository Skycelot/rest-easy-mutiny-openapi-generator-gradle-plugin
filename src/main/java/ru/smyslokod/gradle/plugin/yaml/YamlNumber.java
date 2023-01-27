package ru.smyslokod.gradle.plugin.yaml;

import java.math.BigDecimal;

public class YamlNumber extends YamlRoot {

    private final BigDecimal value;

    public YamlNumber(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
