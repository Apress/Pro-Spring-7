
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 15 - Spring Boot WebSocket"

group = "com.apress.prospring7.boot.fifteen"


dependencies {
    implementation(libs.springBootStarterThymeleaf)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterWebsocket)
    implementation(libs.springBootStarterValidation)

    testImplementation(libs.springBootStarterTest)
}

springBoot {
    mainClass = "$group.Chapter15Application"
}
