
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 17 - Spring Boot Artemis App"

group = "com.apress.prospring7.boot.seventeen"


dependencies {
    implementation(libs.springBootStarterJson)
    implementation(libs.springBootStarterArtemis)
    // using the same version Spring boot uses
    implementation("org.apache.artemis:artemis-jakarta-server:2.50.0")

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.ArtemisApplication"
}
