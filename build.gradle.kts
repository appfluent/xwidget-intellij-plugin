plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "us.appfluent"
version = "0.1.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = true
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2023.2.6", useInstaller = true)

        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.plugins.terminal")

        instrumentationTools()
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")
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
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
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