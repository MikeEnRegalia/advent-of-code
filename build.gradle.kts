plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation ("com.google.code.gson:gson:2.8.8")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}