plugins {
    kotlin("jvm") version "2.1.0"
}

group = "yu.know"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}