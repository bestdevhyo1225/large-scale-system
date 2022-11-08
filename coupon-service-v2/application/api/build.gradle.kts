apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":coupon-service-v2:client:event-publisher"))
    implementation(project(":coupon-service-v2:common"))
    implementation(project(":coupon-service-v2:domain:rds"))
    implementation(project(":coupon-service-v2:domain:redis"))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
