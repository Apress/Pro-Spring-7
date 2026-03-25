
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 19 - Spring AI with Ollama"

group = "com.apress.prospring7.boot.nineteen"


dependencies {
    implementation(libs.springAiOllama)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springAiVectorStore)

    // to allow netty on M1 - M4 -- for other operating systems you might need to customise this
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.9.Final:osx-aarch_64")

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

springBoot {
    mainClass = "$group.SpringAiApplication"
}
