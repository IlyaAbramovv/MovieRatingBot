group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
