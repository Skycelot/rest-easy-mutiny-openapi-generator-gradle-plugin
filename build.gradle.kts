plugins {

    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.1.0"
    id("maven-publish")
}

group = "ru.smyslokod"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.snakeyaml:snakeyaml-engine:2.6")
}

gradlePlugin {
    plugins {
        create("openapiRestEasy") {
            id = "ru.smyslokod.gradle.plugin.openapi.resteasy"
            implementationClass = "ru.smyslokod.gradle.plugin.OpenApiRestEasyPlugin"
        }
    }
}

//tasks.jar {
//    manifest.attributes["Main-Class"] = "ru.smyslokod.gradle.plugin.OpenApiRestEasyPlugin"
//    from(configurations.runtimeClasspath.get().map(::zipTree))
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}
