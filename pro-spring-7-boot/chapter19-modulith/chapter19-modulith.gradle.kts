
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 19 - Spring Modulith"

group = "com.apress.prospring7.boot.nineteen"


dependencies {
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterWeb)

    implementation(libs.springModulithCore)
    implementation(libs.springModulithJpa)

    api(libs.hibernateCore) // to use 7.2.6.Final
    implementation(libs.mariaDB)

    runtimeOnly(libs.springBootStarterActuator)
    runtimeOnly(libs.springModulithActuator)
    runtimeOnly(libs.springModulithObservability)

    testImplementation(libs.springModulithStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.ModulithApplication"
}
