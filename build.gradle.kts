val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val koinKtorVersion: String by project
val koinKspVersion: String by project


plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    kotlin("kapt") version "1.8.10"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

apply {
    plugin("com.google.devtools.ksp")
}

group = "com.kettl"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}


repositories {
    mavenCentral()
}
dependencies {


    // Aws dependencies
    implementation("aws.sdk.kotlin:s3:0.19.0-beta")

    // Koin dependencies
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinKtorVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinKtorVersion")
    ksp("io.insert-koin:koin-ksp-compiler:1.1.1")


    // Scylla dependencies
    kapt("com.scylladb:java-driver-mapper-processor:4.14.1.0")
    implementation("com.scylladb:java-driver-core:4.14.1.0")
    implementation("com.scylladb:java-driver-query-builder:4.14.1.0")
    implementation("com.scylladb:java-driver-mapper-runtime:4.14.1.0")

    // Ktor dependencies
    implementation("io.ktor:ktor-server-request-validation:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:2.2.3")
    implementation("io.ktor:ktor-client-apache-jvm:2.2.3")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")

    // Logging dependencies

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")


    // Misc dependencies
    implementation("de.mkammerer:argon2-jvm-nolibs:2.11")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.7")
}