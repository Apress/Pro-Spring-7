plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 3 - Advanced Spring Configuration and Spring Boot"

group = "com.apress.prospring7.boot.three"

dependencies {
    api(project(":chapter00-boot"))
    implementation(libs.springBootStarter)
    testImplementation(libs.springBootStarterTest)
}

springBoot {
    mainClass = "$group.beans.BeansTest"
}
