description = "Pro Spring 7: Chapter 1 - The Basics (of Spring Classic)"

group = "com.apress.prospring7.classic.four"


// we are using Spring Boot dependency management, but we configure Spring MVC in the classic manner - explicitly, with no Spring Boot "magic"
/*
dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.0-SNAPSHOT")
    }
}
*/


dependencies {
    // spring boot dependency-management does not work here at the moment,
    // because Spring Dependency management does not point at this version yet
    implementation("org.springframework:spring-context:7.0.0-M1")
    implementation("org.springframework:spring-aop:7.0.0-M1")
    implementation("org.springframework:spring-beans:7.0.0-M1")
    implementation("org.springframework:spring-core:7.0.0-M1")
    implementation("org.springframework:spring-expression:7.0.0-M1")

    testImplementation("org.springframework:spring-test:7.0.0-M1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
}
