
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 12 - Validation, Formatting, and Type Conversion"

group = "com.apress.prospring7.boot.twelve"


dependencies {
    implementation(libs.springBootStarterValidation)

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}


springBoot {
    mainClass = "$group.Chapter12Application"
}
