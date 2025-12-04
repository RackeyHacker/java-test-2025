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
    implementation(project(":EntityModule"))
    implementation("com.opencsv:opencsv:5.8")
    implementation(project(":DI"))
    implementation(project(":Config"))
    implementation("org.postgresql:postgresql:42.6.0")
}

tasks.test {
    useJUnitPlatform()
}