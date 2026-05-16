import org.gradle.api.GradleException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

pluginManagement {
    repositories {
        maven(rootDir.resolve("gradle/stonecutter-maven").toURI())
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
    }
}

val stonecutterVersion = "0.9.4"
val stonecutterSourceRepository = "https://codeberg.org/stonecutter/stonecutter"
val stonecutterArtifactRepository = rootDir.resolve("gradle/stonecutter-maven")
val stonecutterArtifactBaseUrl = "https://plugins.gradle.org/m2/dev/kikugie/stonecutter/$stonecutterVersion"
val stonecutterArtifactFiles = listOf(
    "stonecutter-$stonecutterVersion.jar",
    "stonecutter-$stonecutterVersion-sources.jar",
    "stonecutter-$stonecutterVersion.pom",
    "stonecutter-$stonecutterVersion.module"
)

fun syncStonecutterArtifacts() {
    val artifactDirectory = stonecutterArtifactRepository.resolve("dev/kikugie/stonecutter/$stonecutterVersion")
    Files.createDirectories(artifactDirectory.toPath())

    stonecutterArtifactFiles.forEach { fileName ->
        val target = artifactDirectory.resolve(fileName).toPath()
        if (Files.exists(target)) return@forEach

        val artifactUrl = "$stonecutterArtifactBaseUrl/$fileName"
        try {
            artifactUrl.toURL().openStream().use { input ->
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (exception: Exception) {
            throw GradleException(
                "Unable to download missing Stonecutter artifact '$fileName' from $artifactUrl. " +
                    "Restore the vendored copy under gradle/stonecutter-maven or refresh it from $stonecutterSourceRepository.",
                exception
            )
        }
    }
}

syncStonecutterArtifacts()

buildscript {
    repositories {
        maven(rootDir.resolve("gradle/stonecutter-maven").toURI())
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
    }
    dependencies {
        classpath("dev.kikugie:stonecutter:$stonecutterVersion")
    }
}

apply(plugin = "dev.kikugie.stonecutter")

stonecutter {
    create(rootProject) {
        versions(
            "1.19.4", "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4",
            "1.20.5", "1.20.6",
            "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4",
            "1.21.5", "1.21.6", "1.21.7",
            "1.21.8", "1.21.9", "1.21.10", "1.21.11"
        )
    }
}
