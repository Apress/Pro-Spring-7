@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.maven


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
rootProject.name = "pro-spring-7-classic"

include(":chapter01")
findProject(":chapter01")?.name = "chapter01-classic"
include(":chapter02")
findProject(":chapter02")?.name = "chapter02-classic"
include(":chapter03")
findProject(":chapter03")?.name = "chapter03-classic"
include(":chapter04")
findProject(":chapter04")?.name = "chapter04-classic"
include(":chapter05")
findProject(":chapter05")?.name = "chapter05-classic"
include(":chapter06")
findProject(":chapter06")?.name = "chapter06-classic"
include(":chapter06-jooq")
findProject(":chapter06-jooq")?.name = "chapter06-jooq-classic"
include(":chapter07")
findProject(":chapter07")?.name = "chapter07-classic"
include(":chapter08")
findProject(":chapter08")?.name = "chapter08-classic"
include(":chapter09")
findProject(":chapter09")?.name = "chapter09-classic"
include(":chapter09-mongo")
findProject(":chapter09-mongo")?.name = "chapter09-mongo-classic"
include(":chapter10-r2dbc")
findProject(":chapter10-r2dbc")?.name = "chapter10-r2dbc-classic"
include(":chapter10-mongo")
findProject(":chapter10-mongo")?.name = "chapter10-mongo-classic"
include(":chapter11")
findProject(":chapter11")?.name = "chapter11-classic"
include(":chapter12")
findProject(":chapter12")?.name = "chapter12-classic"
include(":chapter13")
findProject(":chapter13")?.name = "chapter13-classic"
include(":chapter13-fct")
findProject(":chapter13-fct")?.name = "chapter13-fct-classic"
include(":chapter14")
findProject(":chapter14")?.name = "chapter14-classic"
include(":chapter15")
findProject(":chapter15")?.name = "chapter15-classic"
include(":chapter16")
findProject(":chapter16")?.name = "chapter16-classic"
include(":chapter18")
findProject(":chapter18")?.name = "chapter18-classic"


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
