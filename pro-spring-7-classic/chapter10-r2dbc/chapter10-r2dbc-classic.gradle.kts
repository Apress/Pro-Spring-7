plugins {
    id("java-library")
}
description = "Pro Spring 7: Chapter 10 - Spring Data R2DBC"

group = "com.apress.prospring7.classic.ten"

dependencies {
    implementation(libs.logback)
    implementation(libs.jakartaAnnotation)

    implementation(libs.springDataR2dbc)
    implementation(libs.springAspects)
    implementation(libs.jacksonDatabind)

    // to allow netty on M1 - M4 -- for other operating systems you might need to customise this
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.9.Final:osx-aarch_64")
    implementation(libs.mariaDBr2dbc)

    testImplementation(libs.reactorTest)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.mariaDB) // needed for Testcontainer to set up the test container
    testImplementation(libs.tcJJ)
    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testImplementation(libs.mockito)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
