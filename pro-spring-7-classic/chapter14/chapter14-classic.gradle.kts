plugins {
    id("java-library")
    id("war")
}
description = "Pro Spring 7: Chapter 14 - Spring REST Apis"

group = "com.apress.prospring7.classic.fourteen"

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
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
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
    manifest.attributes["Specification-Title"] = "Pro Spring 7 - Chapter 14"

    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.war.get() as CopySpec)
}
