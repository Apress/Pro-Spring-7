plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 1 - The Basics (of Spring Boot)"

group = "com.apress.prospring7.boot.one"

dependencies {
   implementation(libs.springBootStarter)
   testImplementation(libs.springBootStarterTest)
}

springBoot {
    mainClass = "$group.Chapter1Application"
}
