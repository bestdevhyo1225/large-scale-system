apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":sns-feed-service:infrastructure:kafka"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
