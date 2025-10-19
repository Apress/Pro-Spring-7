
plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springManagement)
    alias(libs.plugins.jooqPlugin)
}

description = "Pro Spring 7: Chapter 6 - JOOQ (Spring Boot)"

group = "com.apress.prospring7.boot.six"

sourceSets.main.get().java.srcDirs("src/main/java", "src/main/generated")

dependencies {
    implementation(libs.springBootStarterJooq)
    implementation(libs.mariaDB)
    api(libs.hibernateCore)
    implementation(libs.jooqCore) //override boot version

    testImplementation(libs.tcMariaDB)
    testImplementation(libs.tcJJ)
    testImplementation(libs.springBootStarterTest)

    // needed for the jooqCodegen task
    jooqCodegen(libs.jooqCodeGen)
    jooqCodegen(libs.mariaDB)
}

jooq {
    version = libs.versions.jooq
    configurations {
        configuration {
            logging = org.jooq.meta.jaxb.Logging.WARN
            generator {
                name = "org.jooq.codegen.JavaGenerator"

                database {
                    name = " org.jooq.meta.mariadb.MariaDBDatabase"
                    inputSchema = "musicdb"
                    includes = ".*"
                }

                jdbc {
                    driver = "org.mariadb.jdbc.Driver"
                    url = "jdbc:mariadb://localhost:3306/musicdb"
                    user = "prospring7"
                    password = "prospring7"
                }

                target {
                    packageName = "com.apress.prospring7.boot.six.jooq.generated"
                    directory = File(project.projectDir.absolutePath + "/src/main/generated").absolutePath
                }

                generate {
                    pojos = true
                    pojosToString = true
                    daos = true
                }
            }
        }
    }
}

tasks.register<Exec>("buildAndStartJOOQImage") {
    group = "jooq"
    if (!System.getProperty("os.name").lowercase().contains("windows", true)) {
        workingDir("./podman-build")
        commandLine("./start.sh")
    } else {
        workingDir("podman-build")
        commandLine("cmd", "/c", "start.bat")
    }
}

tasks.register("waitForJOOQContainer") {
    group = "jooq"
    dependsOn("buildAndStartJOOQImage")
        doLast {
            println(" ...Waiting for Database to become accessible ...")
            Thread.sleep(20000) // This might need to be bigger
        }
}

tasks.register<Exec>("stopJOOQContainer") {
    group = "jooq"
    println("... stop Container image ...")
    if (!System.getProperty("os.name").lowercase().contains("windows", true)) {
        workingDir("./podman-build")
        commandLine("./stop.sh")
    } else {
        workingDir("podman-build")
        commandLine("cmd", "/c", "stop.bat")
    }
}


tasks.findByPath("jooqCodegen")?.dependsOn("waitForJOOQContainer")

tasks.withType<JavaCompile> {
    dependsOn("jooqCodegen")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn("jooqCodegen")
    // This fixes the Windows naming issues of jars - '(?)'is added to the end of the jar, making the name invalid
    archiveFileName.set("chapter06-jooq-boot.jar")
}

tasks.withType<Test> {
    finalizedBy("stopJOOQContainer")
}

springBoot {
    mainClass = "$group.Chapter6Application"
}
