description = "Pro Spring 7: Chapter 12 - Validation, Formatting, and Type Conversion"

group = "com.apress.prospring7.classic.twelve"

dependencies {
    implementation(libs.springContext)
    implementation(libs.logback)
    api(libs.jakartaAnnotation) // explicit to use 3.0.0, Spring Data JPA brings in 2.0.0
    api(libs.commonsLang3)

    api(libs.hibernateValidator)
    //implementation("org.glassfish:jakarta.el:5.0.0-M1")
    implementation("org.apache.tomcat.embed:tomcat-embed-el:11.0.15")

    testImplementation(libs.springTest)
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitJupiterPlatform)
}
