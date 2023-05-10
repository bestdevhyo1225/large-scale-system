apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service-v2:domain:rds"))
    implementation(project(":sns-feed-service-v2:domain:redis"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
}
