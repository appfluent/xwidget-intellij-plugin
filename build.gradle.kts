import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.models.ProductRelease

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "us.appfluent"
version = "0.1.5"

repositories {
    google()
    mavenCentral()

    // JetBrains cache redirector for IntelliJ artifacts (required for many IJ plugin builds)
    maven { url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies") }
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
    maven { url = uri("https://download.jetbrains.com/teamcity-repository") }

    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = true

    pluginVerification {
        ides {
            select {
                types = listOf(IntelliJPlatformType.IntellijIdeaCommunity)
                channels = listOf(ProductRelease.Channel.RELEASE)
                sinceBuild = "242"
                untilBuild = "252.*"
            }
        }
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.2.5", useInstaller = true)

        bundledPlugins(
            "com.intellij.java",
            "org.jetbrains.plugins.terminal",
        )
        plugins(
            "io.flutter:83.0.4",
            "Dart:252.25557.23"
        )
        pluginVerifier()
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")  // Core Jackson library
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.0")
    implementation("org.yaml:snakeyaml:2.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("com.jetbrains.intellij.platform:test-framework:252.27397.106")
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin") // Specify Kotlin source directory
        java.srcDirs("src/main/kotlin")
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    patchPluginXml {
        sinceBuild.set("242")
        untilBuild.set("252.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

val runIdeFlutter by intellijPlatformTesting.runIde.registering {
    plugins {
        plugin("Dart:252.25557.23")
        plugin("io.flutter:83.0.4")
        plugin("org.jetbrains.android:252.25557.131")
    }
}