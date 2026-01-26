
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 13 - Spring Web Flux"

group = "com.apress.prospring7.boot.thirteen"

dependencies {
    implementation(libs.springBootStarterThymeleaf)
    implementation(libs.springBootStarterWebFlux)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterJson)

    implementation(libs.springBootStarterDataR2dbc)
    implementation(libs.mariaDBr2dbc)
    // to allow netty on M1 - M4 -- for other operating systems you might need to customise this
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.9.Final:osx-aarch_64")

    implementation(libs.commonsIO)

    testImplementation(libs.reactorTest)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.mariaDB) // needed for Testcontainer to set up the test container
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootR2dbcTest)
    testImplementation(libs.springBootStarterTc)
}

springBoot {
    mainClass = "$group.Chapter13Reactive"
}
