import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.models.ProductRelease

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.10.4"
}

group = "us.appfluent"
version = "0.1.8"

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
                types = listOf(IntelliJPlatformType.IntellijIdea)
                channels = listOf(ProductRelease.Channel.RELEASE)
                sinceBuild = "253.*"
                untilBuild = "261.*"
            }
        }
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2025.3")

        bundledPlugins(
            "com.intellij.java",
            "org.jetbrains.plugins.terminal",
        )
        plugins(
            "io.flutter:91.0.0",
            "Dart:504.0.0"
        )
        pluginVerifier()
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.6")  // Core Jackson library
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.6")
    implementation("org.yaml:snakeyaml:2.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("com.jetbrains.intellij.platform:test-framework:261.22158.291")
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
        sinceBuild.set("253")
        untilBuild.set("261.*")
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
        plugin("Dart:504.0.0")
        plugin("io.flutter:91.0.0")
        plugin("org.jetbrains.android:253.30387.90")
    }
}