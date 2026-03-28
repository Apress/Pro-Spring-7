
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
    alias(libs.plugins.graalvmTools)
}

description = "Pro Spring 7: Chapter 19 - Spring for GraalVM"

group = "com.apress.prospring7.boot.nineteen"


dependencies {
    implementation(libs.springBootStarterWeb)

    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.6")
    testImplementation("org.springframework.boot:spring-boot-restclient:4.1.0-M1")
    testRuntimeOnly(libs.junitJupiterPlatform)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterWebMvcTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.NativeApplication"
}

// gradle bootBuildImage
