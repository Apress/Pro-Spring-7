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

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/release") }
    }
    //enforce that only repositories declared in settings.gradle(.kts) are used
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}
rootProject.name = "pro-spring-7"

class ProjectValidationException(message: String) : Exception(message)

include("pro-spring-7-classic")
include("pro-spring-7-boot")
include("pro-spring-7-boot:chapter01")
findProject(":pro-spring-7-boot:chapter01")?.name = "chapter01-boot"
include("pro-spring-7-boot:chapter02")
findProject(":pro-spring-7-boot:chapter02")?.name = "chapter02-boot"
include("pro-spring-7-boot:chapter03")
findProject(":pro-spring-7-boot:chapter03")?.name = "chapter03-boot"
include("pro-spring-7-boot:chapter04")
findProject(":pro-spring-7-boot:chapter04")?.name = "chapter04-boot"
include("pro-spring-7-boot:chapter05")
findProject(":pro-spring-7-boot:chapter05")?.name = "chapter05-boot"

include("pro-spring-7-classic:chapter01")
findProject(":pro-spring-7-classic:chapter01")?.name = "chapter01-classic"
include("pro-spring-7-classic:chapter02")
findProject(":pro-spring-7-classic:chapter02")?.name = "chapter02-classic"
include("pro-spring-7-classic:chapter03")
findProject(":pro-spring-7-classic:chapter03")?.name = "chapter03-classic"
include("pro-spring-7-classic:chapter04")
findProject(":pro-spring-7-classic:chapter04")?.name = "chapter04-classic"
include("pro-spring-7-classic:chapter05")
findProject(":pro-spring-7-classic:chapter05")?.name = "chapter05-classic"


println("""
            >> This project is a collection of simple code samples.
            >> It is meant to be used together with the "Pro Spring 7" book published by Apress in order to learn and practice Spring.
        """.trimIndent())



//we validate the project structure
rootProject.children.forEach {
    validateProject(it)
    it.children.forEach { child -> validateProject(child)}
}

fun validateProject(projectDescriptor: ProjectDescriptor){
    val projectName = projectDescriptor.name
    projectDescriptor.buildFileName = "${projectName}.gradle.kts"
    if (!projectDescriptor.projectDir.isDirectory) {
        throw ProjectValidationException("No directory found for project $projectName")
    }
    if (!projectDescriptor.buildFile.exists() || !projectDescriptor.buildFile.isFile) {
        throw ProjectValidationException("No configuration file found for project $projectName")
    }
}

