apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":coupon-service:data:jpa"))
    implementation(project(":coupon-service:domain"))
    implementation(project(":coupon-service:infrastructure:kafka"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.batch:spring-batch-test")
}
