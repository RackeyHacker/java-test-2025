plugins {
    id("java")
}

group = "com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":ViewModule"))
    implementation(project(":ControllerModule"))
    implementation(project(":EntityModule"))
    implementation(project(":Service"))
    implementation(project(":Repository"))
}

tasks.test {
    useJUnitPlatform()
}