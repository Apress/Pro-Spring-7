
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 10 - Spring Data Mongo Reactive"

group = "com.apress.prospring7.boot.ten"


dependencies {
    implementation(libs.springBootStarterDataMongoReactive)

    testImplementation(libs.reactorTest)
    testImplementation(libs.tcMongoDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)

}


springBoot {
    mainClass = "$group.ReactiveMongoApplication"
}
