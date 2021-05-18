val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koinVersion = "3.0.2"

plugins {
    application
    kotlin("jvm") version "1.5.0"
}

group = "br.com"
version = "0.0.1"

application {
    mainClass.set("br.com.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-jackson:$ktor_version")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // MongoDB
    implementation("org.litote.kmongo:kmongo:4.1.2")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation( "io.ktor:ktor-client-mock:$ktor_version")
}