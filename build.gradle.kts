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
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.google.code.gson:gson:2.10")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime")
    // https://mvnrepository.com/artifact/io.strikt/strikt-jvm
    testImplementation("io.strikt:strikt-jvm:0.34.1")

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