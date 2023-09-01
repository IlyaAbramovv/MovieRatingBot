plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.4"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"
val koinVersion = "3.4.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    testImplementation(kotlin("test"))
    runtimeOnly("io.insert-koin:koin-core:$koinVersion")
    implementation("ch.qos.logback:logback-classic:1.4.11")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("LauncherKt")
}