import dev.kikugie.stonecutter.controller.StonecutterControllerExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

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

configure<StonecutterControllerExtension> {
    active("1.21.11")
}

val stonecutter = extensions.getByType<StonecutterControllerExtension>()

tasks.register("chiseledBuild") {
    group = "project"
    dependsOn(stonecutter.tasks.named("build"))
}
