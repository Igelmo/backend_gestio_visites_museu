val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.20"
}

tasks.create("stage") {
    dependsOn("installDist")
}

tasks.register("stag") {
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
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
}
