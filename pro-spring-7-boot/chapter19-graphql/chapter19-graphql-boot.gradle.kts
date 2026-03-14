
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 19 - Spring for GraphQL"

group = "com.apress.prospring7.boot.nineteen"


dependencies {
    implementation(libs.springBootStarterGraphQL)
    implementation(libs.graphQLTools)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJpa)
    api(libs.hibernateCore) // to use 7.2.6.Final
    implementation(libs.mariaDB)
}

springBoot {
    mainClass = "$group.GraphqlApplication"
}
