apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service:data:redis"))
    implementation(project(":sns-feed-service:domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
}
