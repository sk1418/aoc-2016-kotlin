plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
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