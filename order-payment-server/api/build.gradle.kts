apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":order-payment-server:common"))
    implementation(project(":order-payment-server:domain"))
    implementation(project(":order-payment-server:infrastructure:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx:5.3.19")
}
