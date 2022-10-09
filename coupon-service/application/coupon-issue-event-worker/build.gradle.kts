apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":coupon-service:data:jpa"))
    implementation(project(":coupon-service:domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
}
