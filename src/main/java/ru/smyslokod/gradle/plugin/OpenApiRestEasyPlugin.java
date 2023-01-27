package ru.smyslokod.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class OpenApiRestEasyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("Welcome to OpenApi world! Generate Rest Easy resource interface and transfer objects");
        project.getTasks().create("generate", OpenApiRestEasyGenerateTask.class);
    }
}
