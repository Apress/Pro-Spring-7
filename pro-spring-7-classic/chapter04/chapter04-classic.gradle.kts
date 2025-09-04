description = "Pro Spring 7: Chapter 4 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.four"

dependencies {
    implementation(libs.springContext)
    implementation(libs.logback)

    implementation(libs.aspectjweaver)
    implementation(libs.aspectjrt)

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
