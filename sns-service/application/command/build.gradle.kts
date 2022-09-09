apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-service:common"))
    implementation(project(":sns-service:data:jpa"))
    implementation(project(":sns-service:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
