description = "Pro Spring 7: Chapter 3 - The Basics (of Spring Boot)"

group = "com.apress.prospring7.boot.three"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

springBoot {
    mainClass = "$group.MainThree"
}
