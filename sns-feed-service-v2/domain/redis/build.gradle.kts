dependencies {
    implementation(project(":sns-feed-service-v2:common"))
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
    implementation("io.github.resilience4j:resilience4j-retrofit:1.7.1")
}
