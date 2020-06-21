import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.72"
}

group = "com.github.masahito"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.6") // for kotest framework
	testImplementation("io.kotest:kotest-assertions-core-jvm:4.0.6") // for kotest core jvm assertions
	testImplementation("io.kotest:kotest-property-jvm:4.0.6") // for kotest property test
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
