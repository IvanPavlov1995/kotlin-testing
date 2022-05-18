plugins {
    kotlin("jvm") version "1.6.10"
}

group = "com.devexperts"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("io.strikt:strikt-core:0.33.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("ch.tutteli.atrium:atrium-fluent-en_GB:0.17.0")
}

tasks.test {
    useJUnitPlatform()
}