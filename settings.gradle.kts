pluginManagement {
    val kotlinJvmVersion: String by settings
    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinJvmVersion
    }
}

rootProject.name = "advent-of-code"

