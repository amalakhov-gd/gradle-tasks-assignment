plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "2.1.20"
    `maven-publish`
    `jvm-test-suite`
}

group = "com.course.gradle"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

gradlePlugin {
    plugins {
        create("sortPlugin") {
            id = "com.course.gradle.sort-plugin"
            implementationClass = "com.course.gradle.sortplugin.SortFilesPlugin"
        }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
