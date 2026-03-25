plugins {
    id("java-library")
}

defaultTasks = mutableListOf("clean", "build")

allprojects {
    version = "7.0-SNAPSHOT"
    group = "com.apress.prospring7"
    apply {
        plugin("java-library")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
        withSourcesJar()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        jvmArgs = mutableListOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}
