buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/plugins-snapshot") }
        maven { url = uri("https://repo.spring.io/plugins-milestone") }
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    dependencies {
        classpath( "io.spring.gradle:dependency-management-plugin:2.0.0-SNAPSHOT")
        classpath( "org.springframework.boot:spring-boot-gradle-plugin:4.0.0-M1")
    }
}

defaultTasks = mutableListOf("clean", "build")

println(">> Default tasks: $defaultTasks")

subprojects {
    version = "7.0-SNAPSHOT"

    tasks.withType<Test> {
        useJUnitPlatform()
        jvmArgs = mutableListOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
