package ru.smyslokod.gradle.plugin.model;

import java.util.Arrays;
import java.util.Optional;

public class EnumValuator {

    public <T extends Enum> Optional<T> valueOf(Class<T> clazz, String value) {
        String convertedValue = value.replace('-', '_');
        return Arrays.stream(clazz.getEnumConstants())
                .filter(item -> item.name().equalsIgnoreCase(convertedValue))
                .findAny();
    }
}
