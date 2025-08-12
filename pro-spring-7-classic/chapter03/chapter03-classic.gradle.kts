description = "Pro Spring 7: Chapter 3 - Advanced Spring Configuration and Spring Boot"

group = "com.apress.prospring7.classic.four"

dependencies {
    implementation(project(":pro-spring-7-classic:chapter02-classic"))
    implementation("org.springframework:spring-context:7.0.0-M7")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    implementation("org.apache.groovy:groovy-all:4.0.26")

    testImplementation("org.springframework:spring-test:7.0.0-M7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testImplementation("org.mockito:mockito-core:5.17.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

defaultTasks = mutableListOf("clean", "build") // it seems they are not inherited, so if we want to build this module in isolation, we need to redelcare them here

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.apress.prospring7.classic.three.MainThree"
        attributes["Implementation-Version"] = "$version"
        attributes["Created-By"] = "Iuliana Cosmina"
        attributes["Specification-Title"] = "Pro Spring 7 - Chapter 3"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        //.map(::zipTree)
        .map { if (it.isDirectory) it else zipTree(it)}
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
