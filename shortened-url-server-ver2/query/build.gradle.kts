apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":shortened-url-server-ver2:common"))
    implementation(project(":shortened-url-server-ver2:domain:nosql"))
    implementation(project(":shortened-url-server-ver2:domain:rdbms"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx:5.3.19")
}
