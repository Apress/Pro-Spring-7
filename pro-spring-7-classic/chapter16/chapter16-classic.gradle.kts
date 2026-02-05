plugins {
    id("java-library")
    id("war")
}
description = "Pro Spring 7: Chapter 16 - Spring MVC Thymeleaf with Spring Security"

group = "com.apress.prospring7.classic.sixteen"

dependencies {
    implementation(libs.springWebMvc)
    implementation(libs.springSecurityWeb)
    implementation(libs.springSecurityConfig)
    implementation(libs.springDataJpa)
    implementation(libs.thymeleaf)
    implementation(libs.thymeleafTime)
    implementation(libs.thymeleafSecurity)

    implementation(libs.logback)
    implementation(libs.commonsIO)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0

    api(libs.hibernateCore)
    api(libs.hibernateValidator)
    implementation(libs.tomcatEl)

    implementation(libs.mariaDB)
    implementation(libs.hikariCP)

    compileOnly(libs.servletApi)

    testImplementation(libs.servletApi) // check if needed
    testImplementation(libs.springTest)
    testImplementation(libs.hamcrest)
    testImplementation(libs.restAssured)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}


tasks.register<War>( "fatWar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Created-By"] = "Iuliana Cosmina"
    manifest.attributes["Specification-Title"] = "Pro Spring 7 - Chapter 16"

    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.war.get() as CopySpec)
}
