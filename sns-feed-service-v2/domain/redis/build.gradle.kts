dependencies {
    implementation(project(":sns-feed-service-v2:common"))
    implementation("io.github.resilience4j:resilience4j-retrofit:1.7.1")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:2.0.0")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
