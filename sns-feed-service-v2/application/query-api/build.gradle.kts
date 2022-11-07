apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service-v2:common"))
    implementation(project(":sns-feed-service-v2:domain:rds"))
    implementation(project(":sns-feed-service-v2:domain:redis"))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
