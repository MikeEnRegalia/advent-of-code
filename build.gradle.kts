import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion: String by project

plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.mlriess"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation("io.kotest:kotest-assertions-core:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = javaVersion
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}