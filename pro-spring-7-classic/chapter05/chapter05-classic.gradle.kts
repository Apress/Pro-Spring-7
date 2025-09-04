description = "Pro Spring 7: Chapter 5 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.five"


dependencies {
    implementation(libs.springContext)
    implementation(libs.logback)

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
