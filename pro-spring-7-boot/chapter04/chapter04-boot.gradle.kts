plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 4 - AOP (Spring Boot)"

group = "com.apress.prospring7.boot.four"

dependencies {
    implementation(libs.springBootStarterAspectJ)
    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.Chapter4Application"
}
