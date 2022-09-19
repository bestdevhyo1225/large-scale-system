apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service:domain"))
    implementation(project(":sns-feed-service:data:jpa"))
    implementation(project(":sns-feed-service:data:redis"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
