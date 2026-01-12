plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 10 - Spring Reactive MongoDB"

group = "com.apress.prospring7.classic.ten"

dependencies {
    implementation(libs.logback)
    implementation(libs.jakartaAnnotation)

    implementation(libs.reactorCore)
    implementation(libs.mongoDBReactive)
    implementation(libs.springDataMongo)
    implementation(libs.springAspects)

    testImplementation(libs.reactorTest)
    testImplementation(libs.tcMongoDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
