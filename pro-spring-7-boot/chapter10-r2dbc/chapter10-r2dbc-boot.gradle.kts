
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 10 - Spring Data R2DBC"

group = "com.apress.prospring7.boot.ten"


dependencies {
    implementation(libs.springBootStarterDataR2dbc)
    implementation(libs.mariaDBr2dbc)
    // to allow netty on M1 - M4 -- for other operating systems you might need to customise this
    if (!System.getProperty("os.arch").lowercase().contains("aarch64", true)) {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.9.Final:osx-aarch_64")
    }

    testImplementation(libs.reactorTest)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.mariaDB) // needed for Testcontainer to set up the test container
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootR2dbcTest)
    testImplementation(libs.springBootStarterTc)
}


springBoot {
    mainClass = "$group.Chapter10Application"
}
