
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.kotlinJpa)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
}

description = "Pro Spring 7: Chapter 19 - Spring Kotlin Application"

group = "com.apress.prospring7.boot.nineteen"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterWeb)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    api(libs.hibernateCore) // to use 7.3.0.Final
    implementation(libs.mariaDB)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.6")
    testImplementation("org.springframework.boot:spring-boot-restclient:4.1.0-M1")
    testImplementation(libs.tcJJ)
    testImplementation(libs.tcMariaDB)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springBootStarterWebMvcTest)
    testImplementation(libs.springBootStarterTc)
    testRuntimeOnly(libs.junitJupiterPlatform)

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

/*allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}*/

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    mainClass = "$group.KotlinApplication"
}
