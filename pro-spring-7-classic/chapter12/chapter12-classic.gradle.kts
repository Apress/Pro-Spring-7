plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 12 - Validation, Formatting, and Type Conversion"

group = "com.apress.prospring7.classic.twelve"

dependencies {
    implementation(libs.springContext)
    implementation(libs.logback)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0
    api(libs.commonsLang3)

    api(libs.hibernateValidator)
    //implementation("org.glassfish.expressly:expressly:6.0.0")
    implementation(libs.tomcatEl)

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
