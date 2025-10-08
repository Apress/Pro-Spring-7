plugins {
    id("java-library")
}

description = "Pro Spring 7: Chapter 1 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.one"

dependencies{
    implementation(libs.springContext)

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
