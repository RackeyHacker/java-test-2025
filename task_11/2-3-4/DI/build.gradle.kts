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
    implementation("org.reflections:reflections:0.10.2")
    implementation(project(":Config"))
    implementation("org.reflections:reflections:0.10.2")
    runtimeOnly("org.slf4j:slf4j-nop:2.0.9")


}

tasks.test {
    useJUnitPlatform()
}