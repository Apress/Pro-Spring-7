plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 7 - Spring with Jakarta Persistence API"

group = "com.apress.prospring7.classic.seven"

dependencies {
    implementation(libs.logback)
    implementation(libs.springContext)
    implementation(libs.springOrm)
    api(libs.jakartaAnnotation)
    api(libs.hibernateCore)

    implementation(libs.mariaDB)
    implementation(libs.hikariCP)

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
