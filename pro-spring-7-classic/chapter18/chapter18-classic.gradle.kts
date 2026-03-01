plugins {
    id("java-library")
    id("war")
}
description = "Pro Spring 7: Chapter 18 - Spring Rest with JMX monitoring"

group = "com.apress.prospring7.classic.eighteen"

dependencies {
    implementation(libs.springWebMvc)
    implementation(libs.springDataJpa)

    implementation(libs.logback)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0

    api(libs.hibernateCore)
    api(libs.hibernateValidator)
    implementation(libs.tomcatEl)

    implementation(libs.mariaDB)
    implementation(libs.hikariCP)

    compileOnly(libs.servletApi)

    //testImplementation(libs.servletApi) // check if needed
    testImplementation(libs.springTest)
    testImplementation(libs.hamcrest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)

    implementation(libs.jacksonDatabind)
    implementation(libs.jacksonDataformatXml)
}

tasks.register<War>( "fatWar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Created-By"] = "Iuliana Cosmina"
    manifest.attributes["Specification-Title"] = "Pro Spring 7 - Chapter 18"

    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.war.get() as CopySpec)
}
