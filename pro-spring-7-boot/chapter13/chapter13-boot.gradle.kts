
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 13 - Spring Boot MVC"

group = "com.apress.prospring7.boot.thirteen"


dependencies {
    implementation(libs.springBootStarterThymeleaf)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterDataJpa)
    api(libs.hibernateCore) // to use 7.3.3.Final
    implementation(libs.mariaDB)

    implementation(libs.commonsIO)

    testImplementation(libs.tcJJ)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterWebMvcTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.Chapter13Application"
}
