val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val mongoVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
}

group = "br.com.footprint.carbon"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    // Server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")

    // Client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    // Gson Serializer
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    // MongoDB
    implementation("org.litote.kmongo:kmongo:4.1.2")
}