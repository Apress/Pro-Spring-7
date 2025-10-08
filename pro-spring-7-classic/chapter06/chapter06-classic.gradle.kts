plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 6 - Hibernate and Spring Integration"

group = "com.apress.prospring7.classic.six"

dependencies {
    implementation(libs.springContext)
    implementation(libs.springOrm)
    api(libs.jakartaAnnotation)
    api(libs.hibernateCore)

    implementation(libs.logback)
    implementation(libs.mariaDB)
    implementation(libs.hikariCP)

    testImplementation(libs.h2)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
