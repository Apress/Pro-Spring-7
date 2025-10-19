
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 7 - JPA"

group = "com.apress.prospring7.boot.seven"


dependencies {
    implementation(libs.springBootStarterJdbc)
    implementation(libs.springOrm)

    api(libs.hibernateCore)
    implementation(libs.mariaDB)
    implementation(libs.c3p0)

    testImplementation(libs.h2)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
}


springBoot {
    mainClass = "$group.Chapter7Application"
}
