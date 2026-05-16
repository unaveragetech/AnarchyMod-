buildscript {
    repositories {
        maven(rootDir.resolve("gradle/stonecutter-maven").toURI())
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
    }
    dependencies {
        classpath("dev.kikugie:stonecutter:0.9.4")
    }
}

apply(plugin = "dev.kikugie.stonecutter")

stonecutter active "1.21.11"

tasks.register("chiseledBuild") {
    group = "project"
    dependsOn(stonecutter.tasks.named("build"))
}
