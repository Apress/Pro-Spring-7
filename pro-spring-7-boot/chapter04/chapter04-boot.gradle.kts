plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 4 - AOP (Spring Boot)"

group = "com.apress.prospring7.boot.four"

dependencies {
    implementation(libs.springBootStarterAop)
    testImplementation(libs.springBootStarterTest)
}

springBoot {
    mainClass = "$group.Chapter4Application"
}
