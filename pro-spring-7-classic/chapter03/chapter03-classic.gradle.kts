description = "Pro Spring 7: Chapter 3 - Advanced Spring Configuration and Spring Boot"

group = "com.apress.prospring7.classic.four"

dependencies {
    api(project(":chapter02-classic"))
    implementation(libs.springContext)
    implementation(libs.logback)
    api(libs.jakartaAnnotation)
    api(libs.jakartaInject)
    api(libs.groovyAll)

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testImplementation(libs.mockito)
    testRuntimeOnly(libs.junitJupiterPlatform)
}

tasks.withType<Jar> {
    mustRunAfter(":chapter02-classic:jar")
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
