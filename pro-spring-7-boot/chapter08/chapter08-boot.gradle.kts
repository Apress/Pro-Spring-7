
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 8 - Transactions"

group = "com.apress.prospring7.boot.eight"


dependencies {
    implementation(libs.springBootStarterJdbc)
    implementation(libs.springOrm)

    api(libs.hibernateCore)
    implementation(libs.mariaDB)

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)
}


springBoot {
    mainClass = "$group.Chapter8Application"
}
