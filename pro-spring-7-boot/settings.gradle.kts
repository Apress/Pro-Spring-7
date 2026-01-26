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
/*include(":chapter06")
findProject(":chapter06")?.name = "chapter06-boot"*/
include(":chapter07")
findProject(":chapter07")?.name = "chapter07-boot"
include(":chapter08")
findProject(":chapter08")?.name = "chapter08-boot"
include(":chapter09")
findProject(":chapter09")?.name = "chapter09-boot"
include(":chapter09-mongo")
findProject(":chapter09-mongo")?.name = "chapter09-mongo-boot"
include(":chapter10-r2dbc")
findProject(":chapter10-r2dbc")?.name = "chapter10-r2dbc-boot"
include(":chapter10-mongo")
findProject(":chapter10-mongo")?.name = "chapter10-mongo-boot"
include(":chapter12")
findProject(":chapter12")?.name = "chapter12-boot"
include(":chapter13")
findProject(":chapter13")?.name = "chapter13-boot"
include(":chapter13-reactive")
findProject(":chapter13-reactive")?.name = "chapter13-reactive-boot"
include(":chapter14")
findProject(":chapter14")?.name = "chapter14-boot"

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
