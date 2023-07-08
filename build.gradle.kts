import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion = "5.5.5"


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
//	testImplementation("junit:junit:4.13.1")
    implementation(kotlin("test"))
    implementation("io.kotest:kotest-assertions-core:$kotestVersion")
}