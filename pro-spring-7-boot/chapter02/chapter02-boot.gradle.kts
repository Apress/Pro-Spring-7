plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 2 - The Basics (of Spring Boot)"

group = "com.apress.prospring7.boot.two"

dependencies {
    implementation(libs.springBootStarter)
    testImplementation(libs.springBootStarterTest)
}

springBoot {
    mainClass = "$group.main.Chapter2Application"
}
