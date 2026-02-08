
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 16 - Spring Boot SecuredMVC"

group = "com.apress.prospring7.boot.sixteen"


dependencies {
    implementation(libs.springBootStarterSecurity)
    implementation(libs.springBootStarterThymeleaf)
    implementation(libs.thymeleafSecurity)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterDataJpa)
    api(libs.hibernateCore) // to use 7.2.3.Final
    implementation(libs.mariaDB)

    implementation(libs.commonsIO)

    testImplementation(libs.tcJJ)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.restAssured)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterWebMvcTest)
    testImplementation(libs.springBootStarterTc)
    testImplementation(libs.springSecurityTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.Chapter16Application"
}
