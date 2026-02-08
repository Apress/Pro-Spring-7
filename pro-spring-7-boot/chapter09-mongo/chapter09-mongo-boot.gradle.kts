
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 9 - Spring Data JPA"

group = "com.apress.prospring7.boot.nine"


dependencies {
    implementation(libs.springBootStarterDataMongo)

    testImplementation(libs.tcMongoDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)
}


springBoot {
    mainClass = "$group.Chapter9Application"
}
