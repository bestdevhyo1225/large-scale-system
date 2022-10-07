apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":coupon-service:common"))
    implementation(project(":coupon-service:data:jpa"))
    implementation(project(":coupon-service:data:redis"))
    implementation(project(":coupon-service:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
