
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 9 - Spring Data JPA"

group = "com.apress.prospring7.boot.nine"


dependencies {
    implementation(libs.springBootStarterDataJpa)
    api(libs.hibernateCore) // to use 7.2.3.Final
    implementation(libs.mariaDB)

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)
}


springBoot {
    mainClass = "$group.Chapter9Application"
}
