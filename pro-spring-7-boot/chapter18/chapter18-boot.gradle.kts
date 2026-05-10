
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 18 - Spring Boot REST"

group = "com.apress.prospring7.boot.eighteen"


dependencies {
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterActuator)
    api(libs.hibernateCore) // to use 7.3.3.Final
    implementation(libs.mariaDB)

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.6")
    testImplementation("org.springframework.boot:spring-boot-restclient:4.1.0-M1")
    testImplementation(libs.tcJJ)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterWebMvcTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.Chapter18Application"
}
