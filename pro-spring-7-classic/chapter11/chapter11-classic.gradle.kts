plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 11 - TaskScheduling"

group = "com.apress.prospring7.classic.eleven"

dependencies {
    implementation(libs.logback)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0

    implementation(libs.springDataJpa)

    implementation(libs.mariaDB)
    implementation(libs.hikariCP)
    api(libs.hibernateCore)

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
