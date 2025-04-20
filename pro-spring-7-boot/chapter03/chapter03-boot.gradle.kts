description = "Pro Spring 7: Chapter 3 - Advanced Spring Configuration and Spring Boot"

group = "com.apress.prospring7.boot.three"

dependencies {
    implementation(project(":pro-spring-7-classic:chapter02-classic"))
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

springBoot {
    mainClass = "$group.beans.BeansTest"
}