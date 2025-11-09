plugins {
    id("java")
}

group = "com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ModelModule"))
    implementation(project(":EntityModule"))
}

tasks.test {
    useJUnitPlatform()
}