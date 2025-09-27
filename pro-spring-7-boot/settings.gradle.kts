@file:Suppress("UnstableApiUsage")

pluginManagement {
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
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "pro-spring-7-boot"

include(":chapter00")
findProject(":chapter00")?.name = "chapter00-boot"
include(":chapter01")
findProject(":chapter01")?.name = "chapter01-boot"
include(":chapter02")
findProject(":chapter02")?.name = "chapter02-boot"
include(":chapter03")
findProject(":chapter03")?.name = "chapter03-boot"
include(":chapter04")
findProject(":chapter04")?.name = "chapter04-boot"
include(":chapter05")
findProject(":chapter05")?.name = "chapter05-boot"

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/release") }
    }
    //enforce that only repositories declared in settings.gradle(.kts) are used
    repositoriesMode = RepositoriesMode.PREFER_PROJECT
}

class ProjectValidationException(message: String) : Exception(message)

println(
    """
            >> This project is a collection of simple code samples.
            >> It is meant to be used together with the "Pro Spring 7" book published by Apress in order to learn and practice Spring.
        """.trimIndent()
)


//we validate the project structure
rootProject.children.forEach {
    validateProject(it)
    it.children.forEach { child -> validateProject(child) }
}

fun validateProject(projectDescriptor: ProjectDescriptor) {
    val projectName = projectDescriptor.name
    projectDescriptor.buildFileName = "${projectName}.gradle.kts"
    if (!projectDescriptor.projectDir.isDirectory) {
        throw ProjectValidationException("No directory found for project $projectName")
    }
    if (!projectDescriptor.buildFile.exists() || !projectDescriptor.buildFile.isFile) {
        throw ProjectValidationException("No configuration file found for project $projectName")
    }
}
