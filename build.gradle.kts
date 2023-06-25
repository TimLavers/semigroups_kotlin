import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
}
val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib")) 
	testImplementation("junit:junit:4.13.1")
}