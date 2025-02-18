plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks {
    sourceSets {
        main {
            kotlin.srcDirs("src")
        }
    }
    wrapper {
        gradleVersion = "8.11.1"
    }
}