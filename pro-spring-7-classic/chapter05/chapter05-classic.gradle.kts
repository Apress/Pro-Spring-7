plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 5 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.five"


dependencies {
    implementation(libs.springContext)
    implementation(libs.springJdbc)

    api(libs.jakartaAnnotation)
    implementation(libs.logback)
    implementation(libs.mariaDB)
    implementation(libs.c3p0)

    testImplementation(libs.h2)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
