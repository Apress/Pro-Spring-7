plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 5 - JDBC (Spring Boot)"

group = "com.apress.prospring7.boot.five"

dependencies {
    implementation(libs.springBootStarterJdbc)
    implementation(libs.mariaDB)
    implementation(libs.c3p0)

    testImplementation(libs.h2)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    //testImplementation(libs.springBootJdbcTest)
    testImplementation(libs.springBootStarterTc)
}

springBoot {
    mainClass = "$group.Chapter5Application"
}
