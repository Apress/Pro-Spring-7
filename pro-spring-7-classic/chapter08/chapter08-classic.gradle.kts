plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 8 - Spring Transaction Management!"

group = "com.apress.prospring7.classic.eight"

dependencies {
    implementation(libs.logback)
    implementation(libs.springContext)
    implementation(libs.springOrm)
    api(libs.jakartaAnnotation)

    implementation(libs.mariaDB)
    implementation(libs.hikariCP)
    api(libs.hibernateCore)

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
