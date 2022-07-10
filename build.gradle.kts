import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61" 
}
val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib")) 
	testImplementation("junit:junit:4.13.1")
}