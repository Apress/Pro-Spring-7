plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 9 - Spring Data"

group = "com.apress.prospring7.classic.nine"

dependencies {
    implementation(libs.logback)
    implementation(libs.jakartaAnnotation)

    implementation(libs.springDataMongo)
    implementation(libs.springAspects)

    implementation(libs.mongoDB)

    testImplementation(libs.tcMongoDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
