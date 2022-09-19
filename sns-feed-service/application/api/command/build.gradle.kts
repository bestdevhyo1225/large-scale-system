apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service:common"))
    implementation(project(":sns-feed-service:data:jpa"))
    implementation(project(":sns-feed-service:data:redis"))
    implementation(project(":sns-feed-service:domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
}
