
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 17 - Spring Boot Kafka App"

group = "com.apress.prospring7.boot.seventeen"


dependencies {
    implementation(libs.springBootStarterJson)
    implementation(libs.springBootStarterWeb)
    // using the same version Spring boot uses
    implementation(libs.springBootStarterKafka)

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterKafkaTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.KafkaApplication"
}
