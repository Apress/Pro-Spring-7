plugins {
    id("java-library")
    id("war")
}
description = "Pro Spring 7: Chapter 15 - Spring WebSocket"

group = "com.apress.prospring7.classic.fifteen"

dependencies {
    implementation(libs.springWebMvc)
    implementation(libs.springWebSocket)
    implementation(libs.springMessaging)
    implementation("jakarta.websocket:jakarta.websocket-api:2.3.0-M2")
    implementation(libs.thymeleaf)
    //implementation(libs.thymeleafTime)
    implementation(libs.jacksonDatabind)

    implementation(libs.logback)

    compileOnly(libs.servletApi)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0

    testRuntimeOnly(libs.junitJupiterPlatform)
}


tasks.register<War>( "fatWar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Created-By"] = "Iuliana Cosmina"
    manifest.attributes["Specification-Title"] = "Pro Spring 7 - Chapter 15"

    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.war.get() as CopySpec)
}
