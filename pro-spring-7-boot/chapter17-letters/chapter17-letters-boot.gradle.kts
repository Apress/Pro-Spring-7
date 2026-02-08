
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 17 - Spring Boot Letters App"

group = "com.apress.prospring7.boot.seventeen"


dependencies {
    implementation(libs.springBootStarterDataRest)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootH2Console)
    runtimeOnly(libs.h2)

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.SenderApplication"
}
