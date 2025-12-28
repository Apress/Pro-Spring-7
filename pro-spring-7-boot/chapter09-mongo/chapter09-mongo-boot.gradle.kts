
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 9 - Spring Data JPA"

group = "com.apress.prospring7.boot.nine"


dependencies {
    implementation(libs.springBootStarterDataMongo)
    api(libs.hibernateCore) // to use 7.2.0.Final
    implementation(libs.mariaDB)

    testImplementation(libs.tcMongoDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTc)
}


springBoot {
    mainClass = "$group.Chapter7Application"
}
