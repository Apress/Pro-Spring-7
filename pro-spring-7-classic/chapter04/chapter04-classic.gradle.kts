description = "Pro Spring 7: Chapter 4 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.four"

dependencies {
    // spring boot dependency-management does not work here at the moment,
    // because Spring Dependency management does not point at this version yet
    implementation("org.springframework:spring-context:7.0.0-M7")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("org.aspectj:aspectjweaver:1.9.24")
    implementation("org.aspectj:aspectjrt:1.9.24")

    testImplementation("org.springframework:spring-test:7.0.0-M7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

/*tasks.withType<JavaExec>().configureEach {
   jvmArgs = listOf("--add-opens=jdk.proxy2/jdk.proxy2=ALL-UNNAMED")
}*/
