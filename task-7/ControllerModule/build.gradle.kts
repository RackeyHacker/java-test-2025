plugins {
    id("java")
}

group = "com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Service"))
    implementation(project(":EntityModule"))
}

tasks.test {
    useJUnitPlatform()
}