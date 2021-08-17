val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.20"
}

tasks.register("stage") {
    dependsOn("build")
}

group = "fib.museu"
version = "0.0.1"

application {
    mainClass.set("fib.museu.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("mysql", "mysql-connector-java", "8.0.19")
    implementation("org.ktorm:ktorm-support-mysql:3.2.0")
    implementation("org.ktorm:ktorm-core:3.2.0")
    implementation("org.apache.commons:commons-email:1.5")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}
