val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val mongoVersion: String by project
val ktlint by configurations.creating

group = "br.com.footprint.carbon"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

plugins {
    application
    kotlin("jvm") version "1.5.20"
    id("io.gitlab.arturbosch.detekt").version("1.17.1")
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.1")
    }
}

apply(plugin = "io.gitlab.arturbosch.detekt")

repositories {
    mavenCentral()
}

dependencies {
    // Logger
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    // Gson Serializer
    implementation("io.ktor:ktor-gson:$ktorVersion")

    // MongoDB
    implementation("org.litote.kmongo:kmongo:4.1.2")

    // Amazon SDK - SQS
    implementation("software.amazon.awssdk:sqs:2.16.95")

    // Ktlint
    ktlint("com.pinterest:ktlint:0.41.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }


    // Server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")

    // Client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

detekt {
    input = files("src/main/kotlin")
    config = files("detekt-config.yml")
    autoCorrect = true

    reports {
        html {
            enabled = true
            destination = file("build/reports/detekt.html")
        }
    }
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}