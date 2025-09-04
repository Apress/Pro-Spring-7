plugins {
    id("java-library")
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 2 - IoC and DI in Spring"

group = "com.apress.prospring7.classic.two"

dependencies {
    implementation(libs.springContext)
    implementation(libs.logback)
    api(libs.jakartaAnnotation)
    api(libs.jakartaInject)
}

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
