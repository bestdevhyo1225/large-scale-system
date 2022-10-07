dependencies {
    implementation(project(":coupon-service:common"))
    implementation(project(":coupon-service:domain"))

    // spring data redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // resilience4j
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
    implementation("io.github.resilience4j:resilience4j-retrofit:1.7.1")
}
