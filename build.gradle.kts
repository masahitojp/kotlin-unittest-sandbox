import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    id("io.kotlintest") version "1.1.1"
}

group = "com.github.masahito"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

val versions by extra {
    mapOf(
        "kotest" to "4.1.0"
    )
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:${versions["kotest"]}") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core-jvm:${versions["kotest"]}") // for kotest core jvm assertions
    testImplementation("io.kotest:kotest-property-jvm:${versions["kotest"]}") // for kotest property test
}

tasks.withType<Test> {
    useJUnitPlatform()
    // show standard out and standard error of the test JVM(s) on the consoledate
    testLogging {
        showStandardStreams = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
