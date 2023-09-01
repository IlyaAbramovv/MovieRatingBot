plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.4"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"
val koin_version = "3.4.3"
val ktor_version = "2.3.4"
val exposed_version = "0.41.1"
val h2_version = "2.1.214"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    runtimeOnly("io.insert-koin:koin-core:$koin_version")

    implementation("ch.qos.logback:logback-classic:1.4.11")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("org.postgresql:postgresql:42.2.27")


    testImplementation(kotlin("test"))

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