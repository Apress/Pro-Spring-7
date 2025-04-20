description = "Pro Spring 7: Chapter 2 - IoC and DI in Spring"

group = "com.apress.prospring7.classic.two"

dependencies {
    implementation("org.springframework:spring-context:7.0.0-M4")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
}

defaultTasks = mutableListOf("clean", "build") // it seems they are not inherited, so if we want to build this module in isolation, we need to redelcare them here

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.apress.prospring7.classic.two.HelloWorldSpringDI"
        attributes["Implementation-Version"] = "$version"
        attributes["Created-By"] = "Iuliana Cosmina"
        attributes["Specification-Title"] = "Pro Spring 7 - Chapter 2"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        //.map(::zipTree)
        .map { if (it.isDirectory) it else zipTree(it)}
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}